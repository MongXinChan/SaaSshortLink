package com.MongxinChan.SaaSshortLink.admin.service;

import com.MongxinChan.SaaSshortLink.admin.dao.entity.UserDO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserRegisterReqDTO;
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
    Boolean hasUsername(String userName);

    /**
     * 注册用户
     *
     * @param requestParam 注册用户请求参数
     */
    void register(UserRegisterReqDTO requestParam);

}
