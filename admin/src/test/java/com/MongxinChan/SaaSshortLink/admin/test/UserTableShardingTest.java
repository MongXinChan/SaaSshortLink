package com.MongxinChan.SaaSshortLink.admin.test;

public class UserTableShardingTest {

    public static final String SQL = "CREATE TABLE `tuser_%d`  (\n"
            + "  `id` bigint NOT NULL COMMENT 'ID',\n"
            + "  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',\n"
            + "  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',\n"
            + "  `realName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',\n"
            + "  `phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机',\n"
            + "  `mail` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,\n"
            + "  `deleteTime` datetime NULL DEFAULT NULL COMMENT '注销时间',\n"
            + "  `createTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',\n"
            + "  `updateTime` datetime NULL DEFAULT NULL COMMENT '修改时间',\n"
            + "  `delFlag` tinyint(1) NULL DEFAULT NULL COMMENT '删除标识 0：未删除 1已删除',\n"
            + "  PRIMARY KEY (`id`) USING BTREE,\n"
            + "  UNIQUE INDEX `idx_unique_username`(`userName` ASC) USING BTREE\n"
            + ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n", i);
        }
    }
}
