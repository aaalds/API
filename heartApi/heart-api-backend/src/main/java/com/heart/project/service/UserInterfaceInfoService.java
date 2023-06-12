package com.heart.project.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heart.heartApiCommon.model.entity.UserInterfaceInfo;
import com.heart.project.common.BaseResponse;
import com.heart.project.common.ErrorCode;
import com.heart.project.exception.BusinessException;

/**
* @author 李诗豪
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2022-12-06 09:36:29
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 用户接口信息校验
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);

}
