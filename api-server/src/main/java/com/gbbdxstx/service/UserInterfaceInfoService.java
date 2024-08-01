package com.gbbdxstx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gbbdxstx.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.gbbdxstx.entity.UserInterfaceInfo;


/**
* @author 86187
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service
* @createDate 2024-07-30 14:42:26
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 调用接口统计
     */
    boolean invokeCount(long userId, long interfaceInfoId);
}
