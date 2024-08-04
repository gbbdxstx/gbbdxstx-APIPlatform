package com.gbbdxstx.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局接口请求拦截器
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    //返回 true：放行 false：不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String flag = request.getHeader("Gateway-Flag");

        if (flag == null || !flag.equals("gbbdxstx")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // 设置响应内容类型和编码
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // 写入响应体
            response.getWriter().write("您无权限调用此接口");
            return false;
        }
        return true; //true表示放行
    }
}
