package com.MongxinChan.SaaSshortLink.admin.service;

import com.MongxinChan.SaaSshortLink.admin.dao.entity.GroupDO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 新增短链接分组
     *
     * @param groupName 短链接分组名
     */
    void saveGroup(String groupName);

    /**
     * 查询短链接分组
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 修改短链接分组
     *
     * @param requestParam
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);

    /**
     * 删除短链接
     *
     * @param gid 短链接分组标识
     */
    void deleteGroup(String gid);

    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);
}
