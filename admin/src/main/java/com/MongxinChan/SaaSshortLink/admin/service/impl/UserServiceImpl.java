package com.MongxinChan.SaaSshortLink.admin.service.impl;

import static com.MongxinChan.SaaSshortLink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.MongxinChan.SaaSshortLink.admin.common.enums.UserErrorCodeEnum.USER_EXIST;
import static com.MongxinChan.SaaSshortLink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.MongxinChan.SaaSshortLink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.MongxinChan.SaaSshortLink.admin.common.constant.CacheKeys;
import com.MongxinChan.SaaSshortLink.admin.common.convention.exception.ClientException;
import com.MongxinChan.SaaSshortLink.admin.common.enums.UserErrorCodeEnum;
import com.MongxinChan.SaaSshortLink.admin.dao.entity.UserDO;
import com.MongxinChan.SaaSshortLink.admin.dao.mapper.UserMapper;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserLoginReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserRegisterReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserUpdateReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserLoginRespDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.GroupService;
import com.MongxinChan.SaaSshortLink.admin.service.UserService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final GroupService groupService;

    @Override
    public UserRespDTO getUserByUsername(String userName) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, userName);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean availableUserName(String userName) {
        //如果布隆过滤器存在username，说明不可以用
        return !userRegisterCachePenetrationBloomFilter.contains(userName);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if (!availableUserName(requestParam.getUserName())) {
            throw new ClientException(USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUserName());
        try {
            if (lock.tryLock()) {
                try {
                    int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                    if (inserted < 1) {
                        throw new ClientException(USER_SAVE_ERROR);
                    }
                } catch (DuplicateKeyException ex) {
                    throw new ClientException(USER_EXIST);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUserName());
                groupService.saveGroup(requestParam.getUserName(), "默认分组");
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        // TODO 验证当前用户名是否为登录用户
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUserName, requestParam.getUserName());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserName, requestParam.getUserName())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("用户不存在！！！！");
        }
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().
                entries(CacheKeys.LOGIN_PREFIX + requestParam.getUserName());
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            String token = hasLoginMap.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户登陆错误"));
            return new UserLoginRespDTO(token);
        }
        /**
         * Hash
         * Key：login_用户名
         * Value：
         *  Key：token标识
         *  Val：JSON 字符串（用户信息）
         */
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash()
                .put(CacheKeys.LOGIN_PREFIX + requestParam.getUserName(), uuid,
                        JSON.toJSONString(userDO));
        stringRedisTemplate.expire(CacheKeys.LOGIN_PREFIX + requestParam.getUserName(), 30L,
                TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String userName, String token) {
        return stringRedisTemplate.opsForHash().get(CacheKeys.LOGIN_PREFIX + userName, token)
                != null;
    }

    @Override
    public void logout(String userName, String token) {
        if (checkLogin(userName, token)) {
            stringRedisTemplate.delete(CacheKeys.LOGIN_PREFIX + userName);
            return;
        }
        throw new ClientException("用户Token不存在或用户未登录");
    }
}
