package com.MongxinChan.SaaSshortLink.project.dao.mapper;

import com.MongxinChan.SaaSshortLink.project.dao.entity.LinkLocateStatsDO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkStatsReqDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 地区统计访问持久层
 */
public interface LinkLocateStatsMapper extends BaseMapper<LinkLocateStatsDO> {

    /**
     * 记录地区访问监控数据
     */
    @Insert("INSERT INTO tlink_locate_stats (fullShortURL, gid, date, cnt, country, province, city, adcode, createTime, updateTime, delFlag) "
            +
            "VALUES( #{linkLocateStats.fullShortUrl}, #{linkLocateStats.gid}, #{linkLocateStats.date}, #{linkLocateStats.cnt}, #{linkLocateStats.country}, #{linkLocateStats.province}, #{linkLocateStats.city}, #{linkLocateStats.adcode}, NOW(), NOW(), 0) "
            +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkLocateStats.cnt};")
    void shortLinkLocateState(@Param("linkLocateStats") LinkLocateStatsDO linkLocateStatsDO);


    /**
     * 根据短链接获取指定日期内地区监控数据
     */
    @Select("SELECT " +
            "    province, " +
            "    SUM(cnt) AS cnt " +
            "FROM " +
            "    tlink_locate_stats " +
            "WHERE " +
            "    fullShortURL = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    fullShortURL, gid, province;")
    List<LinkLocateStatsDO> listLocateByShortLink(
            @Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内地区监控数据
     */
    @Select("SELECT " +
            "    province, " +
            "    SUM(cnt) AS cnt " +
            "FROM " +
            "    tlink_locate_stats " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    gid, province;")
    List<LinkLocateStatsDO> listLocateByGroup(
            @Param("param") ShortLinkGroupStatsReqDTO requestParam);
}