package com.gbbdxstx.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 拥有调用次数
     */
    private Integer totalNum;

    /**
     * 已调用次数
     */
    private Integer invokeNum;

    private static final long serialVersionUID = 1L;
}