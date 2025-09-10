package com.MongxinChan.SaaSshortLink.admin.dto.req;

import lombok.Data;

/**
 * @author Mongxin
 */
@Data
public class UserUpdateReqDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}