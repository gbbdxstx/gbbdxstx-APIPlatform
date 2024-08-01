package com.gbbdxstx.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 拥有调用次数
     */
    private Integer totalNum;

    /**
     * 已调用次数
     */
    private Integer invokeNum;

    /**
     * 0-正常(默认) 1-禁用(次数用光/短时间疯狂调用等)
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}