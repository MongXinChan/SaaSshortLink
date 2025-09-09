package com.MongxinChan.SaaSshortLink.admin.service.impl;

import static com.MongxinChan.SaaSshortLink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.MongxinChan.SaaSshortLink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

import cn.hutool.core.bean.BeanUtil;
import com.MongxinChan.SaaSshortLink.admin.common.convention.exception.ClientException;
import com.MongxinChan.SaaSshortLink.admin.dao.entity.UserDO;
import com.MongxinChan.SaaSshortLink.admin.dao.mapper.UserMapper;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserRegisterReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 *
 * @author Mongxin
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    @Override
    public UserRespDTO getUserByUsername(String userName) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, userName);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUsername(String userName) {
        return userRegisterCachePenetrationBloomFilter.contains(userName);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if (hasUsername(requestParam.getUserName())) {
            throw new ClientException(USER_NAME_EXIST);
        }
        int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
        if (inserted < 1) {
            throw new ClientException(USER_SAVE_ERROR);
        }
    }


}
