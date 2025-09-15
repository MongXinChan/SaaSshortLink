package com.MongxinChan.SaaSshortLink.project.toolkit;

import static com.MongxinChan.SaaSshortLink.project.common.constant.ShortLinkConstant.DEFAULT_CACHE_VALID_TIME;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import java.util.Date;
import java.util.Optional;

/**
 * 短链接工具类
 */
public class LinkUtil {

    public static long getLinkCacheValidDate(Date valiDate) {
        return Optional.ofNullable(valiDate)
                .map(each -> DateUtil.between(new Date(), each, DateUnit.MS))
                .orElse(DEFAULT_CACHE_VALID_TIME);
    }
}
