package com.MongxinChan.SaaSshortLink.admin.service;

import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * URL 回收站接口层
 */
public interface RecycleBinService {

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数
     */
    Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(
            ShortLinkRecycleBinPageReqDTO requestParam);
}
