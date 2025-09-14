package com.MongxinChan.SaaSshortLink.admin.common.biz.user;

import static com.MongxinChan.SaaSshortLink.admin.common.enums.UserErrorCodeEnum.USER_TOKEN_FAIL;

import cn.hutool.core.util.StrUtil;
import com.MongxinChan.SaaSshortLink.admin.common.constant.CacheKeys;
import com.MongxinChan.SaaSshortLink.admin.common.convention.exception.ClientException;
import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Results;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 用户信息传输过滤器
 */
@Slf4j
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    private static final List<String> ignoreURI = Lists.newArrayList(
            "/api/saas-short-link/admin/v1/user/login",
            "/api/saas-short-link/admin/v1/user/has-username"
    );

    public UserTransmitFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @SneakyThrows//TODO 等待网关完善后修改
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        if (!ignoreURI.contains(requestURI)) {
            String method = httpServletRequest.getMethod();
            if (!(Objects.equals(requestURI, "/api/saas-short-link/admin/v1/user")
                    && Objects.equals(method, "POST"))) {
                String userName = httpServletRequest.getHeader("userName");
                String token = httpServletRequest.getHeader("token");
                log.info("【过滤器】读取Redis key: {}, token: {}", CacheKeys.LOGIN_PREFIX + userName,
                        token);
                if (!StrUtil.isAllNotBlank(userName, token)) {
                    reject((HttpServletResponse) servletResponse);
                    return;
                }
                Object userInfoJsonStr;
                try {
                    userInfoJsonStr = stringRedisTemplate.opsForHash()
                            .get(CacheKeys.LOGIN_PREFIX + userName, token);
                    if (userInfoJsonStr == null) {
                        log.error("在这里g了啊啊啊");
                        throw new ClientException(USER_TOKEN_FAIL);
                    }
                } catch (ClientException ce) {
                    // 明确 token 无效，直接拒绝
                    reject((HttpServletResponse) servletResponse);
                    return;
                } catch (Exception ex) {
                    // 系统异常，打日志再拒绝
                    log.error("Redis query error", ex);
                    reject((HttpServletResponse) servletResponse);
                    return;
                }
                UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr.toString(),
                        UserInfoDTO.class);
                UserContext.setUser(userInfoDTO);
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @SneakyThrows
    private void reject(HttpServletResponse resp) throws IOException {
        log.error("reject User_TOKEN_FAIL");
        returnJson(resp, JSON.toJSONString(Results.failure(new ClientException(USER_TOKEN_FAIL))));
    }
}