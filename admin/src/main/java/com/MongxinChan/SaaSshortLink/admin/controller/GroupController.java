package com.MongxinChan.SaaSshortLink.admin.controller;

import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Results;
import com.MongxinChan.SaaSshortLink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.GroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增短链接分组创建
     */
    @PostMapping("/api/saas-short-link/v1/group")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return Results.success();
    }

    /**
     * 短链接分组查询
     */
    @GetMapping("/api/saas-short-link/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }
}
