package com.feijimiao.xianyuassistant.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 管理员权限验证工具类
 */
@Slf4j
@Component
public class AdminAuthUtils {

    private static String adminToken;

    @Value("${app.admin.token:}")
    public void setAdminToken(String token) {
        AdminAuthUtils.adminToken = token;
    }

    /**
     * 验证管理员权限
     * 从请求头中获取X-Admin-Token进行验证
     *
     * @param headers 请求头Map
     * @return 验证是否通过
     */
    public static boolean verifyPermission(Map<String, String> headers) {
        String token = headers.get("x-admin-token");
        if (adminToken == null || adminToken.isEmpty()) {
            log.warn("管理员令牌未配置，拒绝操作");
            return false;
        }
        return adminToken.equals(token);
    }
}
