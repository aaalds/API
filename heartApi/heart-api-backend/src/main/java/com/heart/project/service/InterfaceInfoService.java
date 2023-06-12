package com.heart.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.heart.heartApiCommon.model.entity.InterfaceInfo;

/**
* @author 李诗豪
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2022-11-28 17:49:19
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
