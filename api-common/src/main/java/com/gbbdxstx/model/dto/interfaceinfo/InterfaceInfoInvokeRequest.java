package com.gbbdxstx.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;


    /**
     * 请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}