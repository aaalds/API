package com.heart.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heart.heartApiCommon.model.entity.InterfaceInfo;
import com.heart.project.common.ErrorCode;
import com.heart.project.exception.BusinessException;
import com.heart.project.mapper.InterfaceInfoMapper;
import com.heart.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 李诗豪
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2022-11-28 17:49:19
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
//        String description = interfaceInfo.getDescription();
//        String url = interfaceInfo.getUrl();
//        String requestHeader = interfaceInfo.getRequestHeader();
//        String responseHeader = interfaceInfo.getResponseHeader();
//        Integer status = interfaceInfo.getStatus();
//        String method = interfaceInfo.getMethod();
//        Long userId = interfaceInfo.getUserId();
//        Date createTime = interfaceInfo.getCreateTime();
//        Date updateTime = interfaceInfo.getUpdateTime();
//        Integer idDelete = interfaceInfo.getIdDelete();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }

    }

}




