package com.gbbdxstx.dto.userinterfaceinfo;

import com.gbbdxstx.request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 搜索词
     */
    private String searchText;

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

    /**
     * 0-正常(默认) 1-禁用(次数用光)
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}