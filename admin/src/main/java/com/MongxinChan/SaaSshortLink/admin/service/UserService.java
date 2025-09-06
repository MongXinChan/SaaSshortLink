package com.MongxinChan.SaaSshortLink.admin.service;

import com.MongxinChan.SaaSshortLink.admin.dao.entity.UserDO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String userName);
}
