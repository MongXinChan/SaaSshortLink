package com.MongxinChan.SaaSshortLink.project.service;

import com.MongxinChan.SaaSshortLink.project.dao.entity.ShortLinkDO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkCreateReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkCreateRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 创建短链接
     *
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建信息
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);
}
