package com.MongxinChan.SaaSshortLink.project.dao.mapper;

import com.MongxinChan.SaaSshortLink.project.dao.entity.LinkOsStatsDO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkStatsReqDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 操作系统统计访问持久层
 */
public interface LinkOsStatsMapper extends BaseMapper<LinkOsStatsDO> {

    /**
     * 记录地区访问监控数据
     */
    @Insert("INSERT INTO tlink_os_stats (fullShortURL, gid, date, cnt, os, create_time, update_time, del_flag) "
            +
            "VALUES( #{linkOsStats.fullShortUrl}, #{linkOsStats.gid}, #{linkOsStats.date}, #{linkOsStats.cnt}, #{linkOsStats.os}, NOW(), NOW(), 0) "
            +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkOsStats.cnt};")
    void shortLinkOsState(@Param("linkOsStats") LinkOsStatsDO linkOsStatsDO);

    /**
     * 根据短链接获取指定日期内操作系统监控数据
     */
    @Select("SELECT " +
            "    os, " +
            "    SUM(cnt) AS count " +
            "FROM " +
            "    tlink_os_stats " +
            "WHERE " +
            "    fullShortURL = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    fullShortURL, gid, os;")
    List<HashMap<String, Object>> listOsStatsByShortLink(
            @Param("param") ShortLinkStatsReqDTO requestParam);


    /**
     * 根据分组获取指定日期内操作系统监控数据
     */
    @Select("SELECT " +
            "    os, " +
            "    SUM(cnt) AS count " +
            "FROM " +
            "    tlink_os_stats " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    gid, os;")
    List<HashMap<String, Object>> listOsStatsByGroup(
            @Param("param") ShortLinkGroupStatsReqDTO requestParam);
}