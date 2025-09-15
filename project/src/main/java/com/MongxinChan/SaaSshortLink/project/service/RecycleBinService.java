package com.MongxinChan.SaaSshortLink.project.service;

import com.MongxinChan.SaaSshortLink.project.dao.entity.ShortLinkDO;
import com.MongxinChan.SaaSshortLink.project.dto.req.RecycleBinRecoverReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.RecycleBinSaveReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 回收站管理接口层
 */
public interface RecycleBinService extends IService<ShortLinkDO> {

    /**
     * 保存回收站
     *
     * @param requestParam 请求参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查询短链接
     *
     * @param requestParam 分页查询请求参数
     * @return 短链接分页返回列表
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);


    /**
     * 恢复短链接
     *
     * @param requestParam 请求参数
     */
    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);
}