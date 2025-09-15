package com.MongxinChan.SaaSshortLink.project.controller;

import com.MongxinChan.SaaSshortLink.project.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.project.common.convention.result.Results;
import com.MongxinChan.SaaSshortLink.project.dto.req.RecycleBinRecoverReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.RecycleBinSaveReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkPageRespDTO;
import com.MongxinChan.SaaSshortLink.project.service.RecycleBinService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站管理控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 保存回收站
     */
    @PostMapping("/api/saas-short-link/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/saas-short-link/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(
            ShortLinkRecycleBinPageReqDTO requestParam) {
        return Results.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     *
     */
    @PostMapping("/api/saas-short-link/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success();
    }
}
