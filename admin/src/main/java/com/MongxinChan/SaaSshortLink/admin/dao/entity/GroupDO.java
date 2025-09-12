package com.MongxinChan.SaaSshortLink.admin.dao.entity;

import com.MongxinChan.SaaSshortLink.admin.database.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接分组实体
 * @author Mongxin
 */
@Data
@TableName("tGroup")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创造短链接的用户名
     */
    private String userName;
}