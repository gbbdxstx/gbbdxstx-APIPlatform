package com.gbbdxstx.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *

 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 批量ids
     */
    private Long[] ids;

    private static final long serialVersionUID = 1L;
}