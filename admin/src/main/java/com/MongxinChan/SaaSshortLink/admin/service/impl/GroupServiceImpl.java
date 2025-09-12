package com.MongxinChan.SaaSshortLink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.MongxinChan.SaaSshortLink.admin.common.biz.user.UserContext;
import com.MongxinChan.SaaSshortLink.admin.dao.entity.GroupDO;
import com.MongxinChan.SaaSshortLink.admin.dao.mapper.GroupMapper;
import com.MongxinChan.SaaSshortLink.admin.database.BaseDO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.GroupService;
import com.MongxinChan.SaaSshortLink.admin.toolkit.RandomGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
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
                .userName(UserContext.getUsername())
                .name(groupName)
                .build();

        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUserName, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, BaseDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);
    }

    @Override
    public void updateGroup(ShortLinkGroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUserName, UserContext.getUsername())
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO, updateWrapper);

    }

    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUserName, UserContext.getUsername())
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);

    }

    @Override
    public void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam) {
        // 构造一个 List<GroupDO> 列表，用于批量更新
        List<GroupDO> groupList = requestParam.stream()
                .map(req -> {
                    GroupDO groupDO = new GroupDO();
                    groupDO.setGid(req.getGid());
                    groupDO.setSortOrder(req.getSortOrder());
                    return groupDO;
                })
                .collect(Collectors.toList());

        // 调用 MyBatis-Plus 的批量更新方法
        // 这里的 saveOrUpdateBatch 方法会根据主键（gid）自动判断是新增还是更新
        // 如果没有批量更新方法，也可以考虑手动拼装 SQL 语句
        saveOrUpdateBatch(groupList);
    }

    private boolean hasGid(String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUserName, UserContext.getUsername());

        // userName需要从网关中获取，目前还没有撰写相关逻辑，后续写后再补上。
        GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag != null;
    }
}
