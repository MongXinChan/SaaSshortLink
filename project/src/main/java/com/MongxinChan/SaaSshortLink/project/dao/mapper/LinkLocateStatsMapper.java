package com.MongxinChan.SaaSshortLink.project.dao.mapper;

import com.MongxinChan.SaaSshortLink.project.dao.entity.LinkLocateStatsDO;
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
            "VALUES( #{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.gid}, #{linkLocaleStats.date}, #{linkLocaleStats.cnt}, #{linkLocaleStats.country}, #{linkLocaleStats.province}, #{linkLocaleStats.city}, #{linkLocaleStats.adcode}, NOW(), NOW(), 0) "
            +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkLocaleStats.cnt};")
    void shortLinkLocateState(@Param("linkLocaleStats") LinkLocateStatsDO linkLocaleStatsDO);


    /**
     * 根据短链接获取指定日期内基础监控数据
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

}