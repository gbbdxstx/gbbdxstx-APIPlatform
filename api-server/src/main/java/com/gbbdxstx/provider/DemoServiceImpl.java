package com.gbbdxstx.provider;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gbbdxstx.enumeration.ErrorCode;
import com.gbbdxstx.exception.BusinessException;
import com.gbbdxstx.exception.ThrowUtils;
import com.gbbdxstx.mapper.InterfaceInfoMapper;
import com.gbbdxstx.mapper.UserInterfaceInfoMapper;
import com.gbbdxstx.mapper.UserMapper;
import com.gbbdxstx.model.entity.InterfaceInfo;
import com.gbbdxstx.model.entity.User;
import com.gbbdxstx.model.entity.UserInterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class DemoServiceImpl implements DemoService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    /**
     * 更新调用次数
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    public boolean invokeCount(long userId, long interfaceInfoId) {
        // 校验
        ThrowUtils.throwIf(userId <= 0 || interfaceInfoId <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("status", 0);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.NO_AUTH_ERROR);
        userInterfaceInfo.setInvokeNum(userInterfaceInfo.getInvokeNum() + 1);
        // 判断是否要更新用户接口状态
        if (userInterfaceInfo.getTotalNum() == userInterfaceInfo.getInvokeNum()) {
            userInterfaceInfo.setStatus(1);
        }
        // 更新数据库 将调用次数 + 1 同时状态改变时更新状态
        return userInterfaceInfoMapper.updateById(userInterfaceInfo) == 1;
    }


    /**
     * 根据ak,sk获得用户信息
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据路径和方法获得接口信息
     * @param url
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }

}