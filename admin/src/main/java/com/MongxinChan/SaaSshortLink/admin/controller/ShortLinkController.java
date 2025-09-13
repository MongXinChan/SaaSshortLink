package com.MongxinChan.SaaSshortLink.admin.controller;

import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.admin.dto.ShortLinkRemoteService;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * 创建短链接
     */
    @PostMapping("/api/saas-short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(
            @RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/saas-short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }

}
