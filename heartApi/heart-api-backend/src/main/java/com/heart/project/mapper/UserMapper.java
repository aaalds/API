package com.heart.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heart.heartApiCommon.model.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.yupi.project.model.domain.User
 */
public interface UserMapper extends BaseMapper<User> {

    public int selectUserCount(@Param(value = "userAccount") String userAccount);

}




