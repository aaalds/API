package com.heart.project.service.innerImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heart.heartApiCommon.model.entity.InterfaceInfo;
import com.heart.heartApiCommon.service.InnerInterfaceInfoService;
import com.heart.project.common.ErrorCode;
import com.heart.project.exception.BusinessException;
import com.heart.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName InnerInterfaceInfoServiceImpl
 * @Description TODO
 * @Author OTTO
 * @Date 2023/1/10 20:01
 */

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Autowired
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url,String method) {
        if(StringUtils.isAnyBlank(url,method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<InterfaceInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(InterfaceInfo::getUri,url)
                .eq(InterfaceInfo::getMethod,method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(lqw);
        return interfaceInfo;
    }
}
