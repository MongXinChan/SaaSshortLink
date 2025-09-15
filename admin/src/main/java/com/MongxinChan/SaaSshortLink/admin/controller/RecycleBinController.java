package com.MongxinChan.SaaSshortLink.admin.controller;

import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Results;
import com.MongxinChan.SaaSshortLink.admin.dto.req.RecycleBinSaveReqDTO;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.ShortLinkRemoteService;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.MongxinChan.SaaSshortLink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站管理控制层
 *
 * @author Mongxin
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * 保存回收站
     */
    @PostMapping("/api/saas-short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/saas-short-link/admin/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(
            ShortLinkRecycleBinPageReqDTO requestParam) {
        return shortLinkRemoteService.pageRecycleBinShortLink(requestParam);
    }
}