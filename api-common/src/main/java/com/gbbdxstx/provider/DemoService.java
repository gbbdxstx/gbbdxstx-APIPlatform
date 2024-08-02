
package com.gbbdxstx.provider;

import com.gbbdxstx.model.entity.InterfaceInfo;
import com.gbbdxstx.model.entity.User;

public interface DemoService {

    String sayHello(String name);

    /**
     * 调用接口统计
     */
    boolean invokeCount(long userId, long interfaceInfoId);

    /**
     * 根据ak,sk获取用户信息
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

    /**
     * 根据请求路径和方法获取接口信息
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
