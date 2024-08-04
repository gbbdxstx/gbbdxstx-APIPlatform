package com.gbbdxstx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gbbdxstx.annotation.AuthCheck;
import com.gbbdxstx.constant.InterfaceConstant;
import com.gbbdxstx.constant.UserConstant;
import com.gbbdxstx.enumeration.ErrorCode;
import com.gbbdxstx.exception.BusinessException;
import com.gbbdxstx.exception.ThrowUtils;
import com.gbbdxstx.gbbdxstxapiclientsdk.client.ApiClient;
import com.gbbdxstx.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.gbbdxstx.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.gbbdxstx.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.gbbdxstx.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.gbbdxstx.model.entity.InterfaceInfo;
import com.gbbdxstx.model.entity.User;
import com.gbbdxstx.model.vo.InterfaceInfoVO;
import com.gbbdxstx.request.DeleteRequest;
import com.gbbdxstx.request.IdRequest;
import com.gbbdxstx.response.BaseResponse;
import com.gbbdxstx.service.InterfaceInfoService;
import com.gbbdxstx.service.UserService;
import com.gbbdxstx.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 接口管理接口
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private ApiClient apiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验信息
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 批量删除
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/batchDelete")
    public BaseResponse<Boolean> batchDeleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getIds() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        Long[] ids = deleteRequest.getIds();
        // 判断是否存在
        for (Long id : ids) {
            InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
            ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        //if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
        //    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        //}
        boolean b = interfaceInfoService.removeByIds(Arrays.asList(ids));
        return ResultUtils.success(b);
    }


    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVO(interfaceInfo, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    //@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                         HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                           HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        interfaceInfoQueryRequest.setUserId(loginUser.getId());
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    /**
     * 发布接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 判断接口是否可以调用
        com.gbbdxstx.gbbdxstxapiclientsdk.model.User user = new com.gbbdxstx.gbbdxstxapiclientsdk.model.User();
        user.setUsername("小宋");
        String username = apiClient.getUsernameByPost(user);
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        // 修改数据库中接口的状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceConstant.ONLINE);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 修改数据库中接口的状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceConstant.OFFLINE);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 测试调用
     *
     * @param interfaceInfoInvokeRequest
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(oldInterfaceInfo.getStatus() != InterfaceConstant.ONLINE, ErrorCode.FORBIDDEN_ERROR);
        // 调用接口
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        //Gson gson = new Gson();
        //com.gbbdxstx.gbbdxstxapiclientsdk.model.User user = gson.fromJson(userRequestParams, com.gbbdxstx.gbbdxstxapiclientsdk.model.User.class);
        ApiClient tempApiClient = new ApiClient(accessKey, secretKey);
        //String usernameByPost = tempApiClient.getUsernameByPost(user);
        String usernameByPost = tempApiClient.getNameByGet(userRequestParams);
        return ResultUtils.success(usernameByPost);
    }


    // endregion

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    /*@PostMapping("/search/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> searchInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
            HttpServletRequest request) {
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.searchFromEs(interfaceInfoQueryRequest);
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }*/

}
