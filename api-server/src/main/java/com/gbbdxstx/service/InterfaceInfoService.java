package com.gbbdxstx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gbbdxstx.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.gbbdxstx.entity.InterfaceInfo;
import com.gbbdxstx.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86187
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-07-28 14:02:54
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);
}
