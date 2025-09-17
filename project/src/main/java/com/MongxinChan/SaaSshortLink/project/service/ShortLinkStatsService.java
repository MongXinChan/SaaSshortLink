package com.MongxinChan.SaaSshortLink.project.service;


import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkStatsReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkStatsRespDTO;

/**
 * 短链接监控接口层
 */
public interface ShortLinkStatsService {

    /**
     * 获取单个短链接监控数据
     *
     * @param requestParam 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);
}