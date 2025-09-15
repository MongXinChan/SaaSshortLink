package com.MongxinChan.SaaSshortLink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.MongxinChan.SaaSshortLink.admin.common.biz.user.UserContext;
import com.MongxinChan.SaaSshortLink.admin.common.convention.exception.ServiceException;
import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.admin.dao.entity.GroupDO;
import com.MongxinChan.SaaSshortLink.admin.dao.mapper.GroupMapper;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.ShortLinkRemoteService;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.RecycleBinService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * URL 回收站接口实现层
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final GroupMapper groupMapper;

    /**
     * 后续重构为 SpringCLoud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(
            ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUserName, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("用户无分组信息");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return shortLinkRemoteService.pageRecycleBinShortLink(requestParam);
    }
}
