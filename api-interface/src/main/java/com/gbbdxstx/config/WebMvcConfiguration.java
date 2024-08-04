package com.gbbdxstx.config;

import com.gbbdxstx.interceptor.GlobalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Resource
    private GlobalInterceptor globalInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 扩展Spring MVC框架的消息转换器
     *
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                // 过滤出StringHttpMessageConverter类型实例
                .filter(StringHttpMessageConverter.class::isInstance)
                .map(c -> (StringHttpMessageConverter) c)
                // 这里将转换器的默认编码设置为utf-8
                .forEach(c -> c.setDefaultCharset(StandardCharsets.UTF_8));
    }
}
