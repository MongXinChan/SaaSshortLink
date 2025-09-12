package com.MongxinChan.SaaSshortLink.project.common.database;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import lombok.Data;
import org.apache.calcite.avatica.remote.Service;

/**
 * 数据库基础层对象属性
 *
 * @author Mongxin
 */
@Data
public class BaseDO extends Service.Base {

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

}
