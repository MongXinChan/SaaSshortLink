package com.MongxinChan.SaaSshortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.MongxinChan.SaaSshortLink.admin.dao.entity.GroupDO;
import com.MongxinChan.SaaSshortLink.admin.dao.mapper.GroupMapper;
import com.MongxinChan.SaaSshortLink.admin.database.BaseDO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.GroupService;
import com.MongxinChan.SaaSshortLink.admin.toolkit.RandomGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 短链接分组接口实现
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveGroup(String groupName) {
        String gid;
        do {
            gid = RandomGenerator.generateRandom();
        } while (hasGid(gid));
        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .name(groupName)
                .build();

        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        //TODO 从当前的请求中获取用户名,这里还没实现逻辑,用伪代码实现
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUserName, "Jobstone")
                .orderByDesc(GroupDO::getSortOrder, BaseDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);
    }

    private boolean hasGid(String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                //TODO 设置用户名
                .eq(GroupDO::getUserName, null);

        // userName需要从网关中获取，目前还没有撰写相关逻辑，后续写后再补上。
        GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag != null;
    }
}
