
public class UserTableShardingTest {

    public static final String SQL = "DROP TABLE IF EXISTS `tlink_%d`;\n"
            + "CREATE TABLE `tlink_%d`  (\n"
            + "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',\n"
            + "  `domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '域名',\n"
            + "  `shortURI` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '短链接',\n"
            + "  `fullShortURL` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '完整短链接',\n"
            + "  `originURL` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原始连接',\n"
            + "  `clickNum` int NULL DEFAULT 0 COMMENT '点击量',\n"
            + "  `gid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分组标识',\n"
            + "  `favicon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网站图标',\n"
            + "  `enableStatus` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用: 0:启用 1:未启用',\n"
            + "  `createdType` tinyint(1) NULL DEFAULT NULL COMMENT '创建类型: 0:接口创建 1:控制台创建',\n"
            + "  `validDateType` tinyint(1) NULL DEFAULT NULL COMMENT '有效期: 0:永久有效 1:自定义',\n"
            + "  `validDate` datetime NULL DEFAULT NULL COMMENT '有效期',\n"
            + "  `describe` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',\n"
            + "  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',\n"
            + "  `updateTime` datetime NULL DEFAULT NULL COMMENT '修改时间',\n"
            + "  `delFlag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被删除 0:未被删除 1:已删除',\n"
            + "  PRIMARY KEY (`id`, `delFlag`) USING BTREE,\n"
            + "  UNIQUE INDEX `idx_unique_full_short_url`(`fullShortURL` ASC) USING BTREE\n"
            + ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;\n";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n", i, i);
        }
    }
}
