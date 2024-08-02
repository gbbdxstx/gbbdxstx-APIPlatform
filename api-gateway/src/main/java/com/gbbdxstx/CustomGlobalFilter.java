package com.gbbdxstx;

import com.gbbdxstx.gbbdxstxapiclientsdk.utils.SignUtil;
import com.gbbdxstx.model.entity.InterfaceInfo;
import com.gbbdxstx.model.entity.User;
import com.gbbdxstx.provider.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局请求过滤器
 */
@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private DemoService demoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("0:0:0:0:0:0:0:1", "127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        // 获得请求到网关的完整url信息
        String originUrl = request.getURI().toString();
        // 获得网关转发后的路由信息
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String urlBefore = route.getUri().toString();
        String url = urlBefore +  request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识: {}", request.getId());
        log.info("请求路径: {}", url);
        log.info("请求方法: {}", method);
        log.info("请求参数: {}", request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址: {}", sourceAddress);
        ServerHttpResponse response = exchange.getResponse();

        // 2.黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // 3. 用户鉴权(判断 ak,sk 是否合法
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String encodedBody = headers.getFirst("body");
        String body = URLDecoder.decode(encodedBody, StandardCharsets.UTF_8);
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign"); // 用户传过来的密钥
        // todo 实际情况去数据库中查询是否分配给用户
        User invokeUser = null;
        try {
            invokeUser = demoService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        // todo 随机数nonce 利用hashmap或redis存进行校验
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        // todo 时间和当前时间不能超过 5 分钟
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L;
        if (currentTime - Long.parseLong(timestamp) > FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // 将分配给用户的secretKey从数据库查出来用于生成sign
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtil.getSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // 判断请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = demoService.getInterfaceInfo(url, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        // 5. 请求转发, 调用模拟接口
        Mono<Void> filter = chain.filter(exchange);

        // 6. 响应日志
        log.info("响应: {}", response.getStatusCode());
        Long userId = invokeUser.getId();
        Long interfaceInfoId = interfaceInfo.getId();
        try {
            DataBufferFactory bufferFactory = response.bufferFactory();
            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 7. 调用成功, 调用次数加1
                            try {
                                demoService.invokeCount(userId, interfaceInfoId);
                            } catch (Exception e) {
                                log.error("invokeCount error", e);
                            }
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer buff = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[buff.readableByteCount()];
                            buff.read(content);
                            DataBufferUtils.release(buff);//释放掉内存

                            // 构建返回日志
                            String result = new String(content);
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(response.getStatusCode().value());
                            rspArgs.add(exchange.getRequest().getURI());
                            rspArgs.add(result);
                            log.info("响应结果: <-- {} {}\n{}", rspArgs.toArray());

                            getDelegate().getHeaders().setContentLength(result.getBytes().length);
                            return bufferFactory.wrap(result.getBytes());
                        }));
                    } else {
                        // 8. 调用失败, 返回一个标准的错误码
                        handleInvokeError(response);
                        log.error("<-- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());

        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}