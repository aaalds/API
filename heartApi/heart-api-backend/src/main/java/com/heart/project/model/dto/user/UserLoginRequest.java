package com.heart.project.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author yupi
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @TableField("userAccount")
    private String userAccount;
    @TableField("userPassword")
    private String userPassword;
}
