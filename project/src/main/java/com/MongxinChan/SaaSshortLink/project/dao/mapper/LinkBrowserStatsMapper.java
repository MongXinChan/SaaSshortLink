package com.MongxinChan.SaaSshortLink.project.dao.mapper;


import com.MongxinChan.SaaSshortLink.project.dao.entity.LinkBrowserStatsDO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkStatsReqDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 浏览器统计访问持久层
 */
public interface LinkBrowserStatsMapper extends BaseMapper<LinkBrowserStatsDO> {

    /**
     * 记录浏览器访问监控数据
     */
    @Insert("INSERT INTO tlink_browser_stats (fullShortURL, gid, date, cnt, browser, createTime, updateTime, delFlag) "
            +
            "VALUES( #{linkBrowserStats.fullShortUrl}, #{linkBrowserStats.gid}, #{linkBrowserStats.date}, #{linkBrowserStats.cnt}, #{linkBrowserStats.browser}, NOW(), NOW(), 0) "
            +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkBrowserStats.cnt};")
    void shortLinkBrowserState(@Param("linkBrowserStats") LinkBrowserStatsDO linkBrowserStatsDO);


    /**
     * 根据短链接获取指定日期内浏览器监控数据
     */
    @Select("SELECT " +
            "    browser, " +
            "    SUM(cnt) AS count " +
            "FROM " +
            "    tlink_browser_stats " +
            "WHERE " +
            "    fullShortURL = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    fullShortURL, gid, date, browser;")
    List<HashMap<String, Object>> listBrowserStatsByShortLink(
            @Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内浏览器监控数据
     */
    @Select("SELECT " +
            "    browser, " +
            "    SUM(cnt) AS count " +
            "FROM " +
            "    tlink_browser_stats " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    gid, date, browser;")
    List<HashMap<String, Object>> listBrowserStatsByGroup(
            @Param("param") ShortLinkGroupStatsReqDTO requestParam);
}