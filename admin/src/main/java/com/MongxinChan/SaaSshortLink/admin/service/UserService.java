package com.MongxinChan.SaaSshortLink.admin.service;

import com.MongxinChan.SaaSshortLink.admin.dao.entity.UserDO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserLoginReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserRegisterReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserUpdateReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserLoginRespDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户接口层
 *
 * @author Mongxin
 */
public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String userName);

    /**
     * 查询用户名是否存在
     *
     * @param userName 用户名
     * @return 用户名存在返回 True,反之 False
     */
    Boolean availableUserName(String userName);

    /**
     * 注册用户
     *
     * @param requestParam 注册用户请求参数
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 修改用户
     *
     * @param requestParam
     */
    void update(UserUpdateReqDTO requestParam);

    /**
     * 登录
     *
     * @param requestParam
     * @return
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检查登录
     *
     * @param token
     * @return
     */
    Boolean checkLogin(String token, String userName);

    void logout(String userName, String token);
}
