package com.gbbdxstx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gbbdxstx.constant.CommonConstant;
import com.gbbdxstx.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.gbbdxstx.model.entity.UserInterfaceInfo;
import com.gbbdxstx.enumeration.ErrorCode;
import com.gbbdxstx.exception.BusinessException;
import com.gbbdxstx.exception.ThrowUtils;
import com.gbbdxstx.mapper.UserInterfaceInfoMapper;
import com.gbbdxstx.service.UserInterfaceInfoService;
import com.gbbdxstx.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service实现
* @createDate 2024-07-30 14:42:26
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();

        // 创建时，参数不能为空 且 userId和interfaceId要是有效的
        if (add) {
            ThrowUtils.throwIf(userId == null || interfaceInfoId == null, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(userId <= 0 || interfaceInfoId <= 0, ErrorCode.PARAMS_ERROR);
            // todo 验证两者是否是有效数据
        }
        // 有参数则校验
        if (userInterfaceInfo.getTotalNum() < 0 || userInterfaceInfo.getInvokeNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "次数分配错误, 次数不能小于 0");
        }
    }

    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (userInterfaceInfoQueryRequest == null) {
            return queryWrapper;
        }

        String searchText = userInterfaceInfoQueryRequest.getSearchText();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfoQueryRequest.getTotalNum();
        Integer invokeNum = userInterfaceInfoQueryRequest.getInvokeNum();
        Integer status = userInterfaceInfoQueryRequest.getStatus();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
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
        UserInterfaceInfo userInterfaceInfo = this.getOne(queryWrapper);
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.NO_AUTH_ERROR);
        userInterfaceInfo.setInvokeNum(userInterfaceInfo.getInvokeNum() + 1);
        // 判断是否要更新用户接口状态
        if (userInterfaceInfo.getTotalNum() == userInterfaceInfo.getInvokeNum()) {
            userInterfaceInfo.setStatus(1);
        }
        // 更新数据库 将调用次数 + 1 同时状态改变时更新状态
        return this.updateById(userInterfaceInfo);
    }
}




