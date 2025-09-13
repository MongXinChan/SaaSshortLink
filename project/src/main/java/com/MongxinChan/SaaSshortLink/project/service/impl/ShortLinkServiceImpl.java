package com.MongxinChan.SaaSshortLink.project.service.impl;

import cn.hutool.core.text.StrBuilder;
import com.MongxinChan.SaaSshortLink.project.common.convention.exception.ServiceException;
import com.MongxinChan.SaaSshortLink.project.dao.entity.ShortLinkDO;
import com.MongxinChan.SaaSshortLink.project.dao.mapper.ShortLinkMapper;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkCreateReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkCreateRespDTO;
import com.MongxinChan.SaaSshortLink.project.service.ShortLinkService;
import com.MongxinChan.SaaSshortLink.project.toolkit.HashUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements
        ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    private String generatedSuffix(ShortLinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String shortURI;
        while (true) {
            if (customGenerateCount > 10) {
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }
            String originalURL = requestParam.getOriginUrl();
            shortURI = HashUtil.hashToBase62(originalURL);
            if (!shortUriCreateCachePenetrationBloomFilter.contains(
                    requestParam.getDomain() + "/" + shortURI)) {
                break;
            }
            customGenerateCount++;
        }

        String originUrl = requestParam.getOriginUrl();
        originUrl += System.currentTimeMillis();
        shortURI = HashUtil.hashToBase62(originUrl);
        return HashUtil.hashToBase62(originUrl);
    }

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generatedSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(requestParam.getDomain())
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(requestParam.getDomain())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createdType(requestParam.getCreatedType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortUrl)
                .build();
        try {
            baseMapper.insert(shortLinkDO);
        } catch (DuplicateKeyException ex) {
            // TODO 已经误判的短链接如何处理
            // 1.短链接真实存在缓存中
            // 2.短链接不一定在缓存中
            LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl);
            ShortLinkDO hasShortLinkDO = baseMapper.selectOne(queryWrapper);
            if (hasShortLinkDO != null) {
                log.warn("短链接{} 重复入库", fullShortUrl);
                throw new ServiceException("短链接生成重复");
            }
        }
        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(fullShortUrl)
                .originUrl(requestParam.getOriginUrl())
                .build();
    }
}
