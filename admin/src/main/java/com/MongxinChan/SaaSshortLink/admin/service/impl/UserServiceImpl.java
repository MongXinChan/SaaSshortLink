package com.MongxinChan.SaaSshortLink.admin.service.impl;

import com.MongxinChan.SaaSshortLink.admin.dao.entity.UserDO;
import com.MongxinChan.SaaSshortLink.admin.dao.mapper.UserMapper;
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
 * @author Mongxin
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

  private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

  @Override
  public UserRespDTO getUserByUsername(String userName) {
    LambdaQueryWrapper<UserDO> queryWrapper =Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUserName,userName);
    UserDO userDO =baseMapper.selectOne(queryWrapper);
    UserRespDTO result = new UserRespDTO();
    BeanUtils.copyProperties(userDO,result);
    return result;
  }

  @Override
  public Boolean hasUsername(String userName) {
    return userRegisterCachePenetrationBloomFilter.contains(userName);
  }


}
