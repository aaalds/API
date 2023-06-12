package com.heart.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heart.heartApiCommon.model.entity.InterfaceInfo;
import com.heart.heartApiCommon.model.entity.UserInterfaceInfo;
import com.heart.project.annotation.AuthCheck;
import com.heart.project.common.BaseResponse;
import com.heart.project.common.ErrorCode;
import com.heart.project.common.ResultUtils;
import com.heart.project.exception.BusinessException;
import com.heart.project.mapper.InterfaceInfoMapper;
import com.heart.project.mapper.UserInterfaceInfoMapper;
import com.heart.project.model.vo.InterfaceVo;
import com.heart.project.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName AnalysisController
 * @Description TODO
 * @Author lish
 * @Date 2023/4/27 9:28
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceVo>> listTopInterfaceInfoInvoke(){
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.listTopInterfaceInfoInvoke(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        LambdaQueryWrapper<InterfaceInfo> lqw =new LambdaQueryWrapper<>();
        lqw.in(InterfaceInfo::getId,interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(lqw);
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceVo> interfaceVos = list.stream().map(interfaceInfo -> {
            InterfaceVo interfaceVo = new InterfaceVo();
            BeanUtils.copyProperties(interfaceInfo, interfaceVo);
            interfaceVo.setTotalNum(interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum());
            return interfaceVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceVos);
    }
}
