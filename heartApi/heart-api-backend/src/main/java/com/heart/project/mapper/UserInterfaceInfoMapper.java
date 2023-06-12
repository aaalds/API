package com.heart.project.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heart.heartApiCommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 李诗豪
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2022-12-06 09:36:29
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {


    List<UserInterfaceInfo> listTopInterfaceInfoInvoke(int limit);
}




