package com.MongxinChan.SaaSshortLink.project.dao.mapper;

import com.MongxinChan.SaaSshortLink.project.dao.entity.LinkAccessLogsDO;
import com.MongxinChan.SaaSshortLink.project.dao.entity.LinkAccessStatsDO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.MongxinChan.SaaSshortLink.project.dto.req.ShortLinkStatsReqDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 访问日志监控持久层
 */
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {

    /**
     * 根据短链接获取指定日期内高频访问IP数据
     */
    @Select("SELECT " +
            "    ip, " +
            "    COUNT(ip) AS count " +
            "FROM " +
            "    tlink_access_logs " +
            "WHERE " +
            "    fullShortURL = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND createTime BETWEEN CONCAT(#{param.startDate},' 00:00:00') and CONCAT(#{param.endDate},' 23:59:59') "
            +
            "GROUP BY " +
            "    fullShortURL, gid, ip " +
            "ORDER BY " +
            "    count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTopIpByShortLink(
            @Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内高频访问IP数据
     */
    @Select("SELECT " +
            "    ip, " +
            "    COUNT(ip) AS count " +
            "FROM " +
            "    tlink_access_logs " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND createTime BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    gid, ip " +
            "ORDER BY " +
            "    count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTopIpByGroup(
            @Param("param") ShortLinkGroupStatsReqDTO requestParam);


    /**
     * 根据短链接获取指定日期内新旧访客数据
     */
    @Select("SELECT " +
            "    SUM(old_user) AS oldUserCnt, " +
            "    SUM(new_user) AS newUserCnt " +
            "FROM ( " +
            "    SELECT " +
            "        CASE WHEN COUNT(DISTINCT DATE(createTime)) > 1 THEN 1 ELSE 0 END AS old_user, "
            +
            "        CASE WHEN COUNT(DISTINCT DATE(createTime)) = 1 AND MAX(createTime) >= #{param.startDate} AND MAX(createTime) <= #{param.endDate} THEN 1 ELSE 0 END AS new_user "
            +
            "    FROM " +
            "        tlink_access_logs " +
            "    WHERE " +
            "        fullShortURL = #{param.fullShortUrl} " +
            "        AND gid = #{param.gid} " +
            "    GROUP BY " +
            "        user " +
            ") AS user_counts;")
    HashMap<String, Object> findUvTypeCntByShortLink(
            @Param("param") ShortLinkStatsReqDTO requestParam);


    /**
     * 获取用户信息是否新老访客
     */
    @Select("<script> " +
            "SELECT " +
            "    user, " +
            "    CASE " +
            "        WHEN MIN(createTime) BETWEEN #{startDate} AND #{endDate} THEN '新访客' " +
            "        ELSE '老访客' " +
            "    END AS uvType " +
            "FROM " +
            "    tlink_access_logs " +
            "WHERE " +
            "    fullShortURL = #{fullShortUrl} " +
            "    AND gid = #{gid} " +
            "    AND user IN " +
            "    <foreach item='item' index='index' collection='userAccessLogsList' open='(' separator=',' close=')'> "
            +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    user;" +
            "    </script>"
    )
    List<Map<String, Object>> selectUvTypeByUsers(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("userAccessLogsList") List<String> userAccessLogsList
    );

    /**
     * 获取分组用户信息是否新老访客
     */
    @Select("<script> " +
            "SELECT " +
            "    user, " +
            "    CASE " +
            "        WHEN MIN(createTime) BETWEEN #{startDate} AND #{endDate} THEN '新访客' " +
            "        ELSE '老访客' " +
            "    END AS uvType " +
            "FROM " +
            "    tlink_access_logs " +
            "WHERE " +
            "    gid = #{gid} " +
            "    AND user IN " +
            "    <foreach item='item' index='index' collection='userAccessLogsList' open='(' separator=',' close=')'> "
            +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    user;" +
            "    </script>"
    )
    List<Map<String, Object>> selectGroupUvTypeByUsers(
            @Param("gid") String gid,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("userAccessLogsList") List<String> userAccessLogsList
    );

    /**
     * 根据短链接获取指定日期内PV、UV、UIP数据
     */
    @Select("SELECT " +
            "    COUNT(user) AS pv, " +
            "    COUNT(DISTINCT user) AS uv, " +
            "    COUNT(DISTINCT ip) AS uip " +
            "FROM " +
            "    tlink_access_logs " +
            "WHERE " +
            "    fullShortURL = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND createTime BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    fullShortURL, gid;")
    LinkAccessStatsDO findPvUvUidStatsByShortLink(
            @Param("param") ShortLinkStatsReqDTO requestParam);


    /**
     * 根据分组获取指定日期内PV、UV、UIP数据
     */
    @Select("SELECT " +
            "    COUNT(user) AS pv, " +
            "    COUNT(DISTINCT user) AS uv, " +
            "    COUNT(DISTINCT ip) AS uip " +
            "FROM " +
            "    tlink_access_logs " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND createTime BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    gid;")
    LinkAccessStatsDO findPvUvUidStatsByGroup(
            @Param("param") ShortLinkGroupStatsReqDTO requestParam);
}