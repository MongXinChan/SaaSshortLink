package com.MongxinChan.SaaSshortLink.project.controller;

import com.MongxinChan.SaaSshortLink.project.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.project.common.convention.result.Results;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkCreateReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkPageReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkUpdateReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkCreateRespDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkPageRespDTO;
import com.MongxinChan.SaaSshortLink.project.service.ShortLinkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接控制层
 * @author Mongxin
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /***
     *
     */
    @GetMapping("/${short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri,
            ServletRequest request, ServletResponse response) {
        shortLinkService.restoreUrl(shortUri, request, response);
    }

    /**
     * 创建短链接
     */
    @PostMapping("/api/saas-short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(
            @RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/saas-short-link/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/saas-short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }

    /**
     * 短链接分组
     */
    @GetMapping("/api/saas-short-link/v1/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(
            @RequestParam("requestParam") List<String> requestParam) {
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }
}
