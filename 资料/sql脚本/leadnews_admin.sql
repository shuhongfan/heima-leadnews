/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : leadnews_admin

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 26/07/2021 01:45:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ad_article_statistics
-- ----------------------------
DROP TABLE IF EXISTS `ad_article_statistics`;
CREATE TABLE `ad_article_statistics`  (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `article_we_media` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '主账号ID',
  `article_crawlers` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '子账号ID',
  `channel_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '频道ID',
  `read_20` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '草读量',
  `read_100` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '读完量',
  `read_count` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '阅读量',
  `comment` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '评论量',
  `follow` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '关注量',
  `collection` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '收藏量',
  `forward` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '转发量',
  `likes` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '点赞量',
  `unlikes` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '不喜欢',
  `unfollow` int(11) UNSIGNED NULL DEFAULT NULL COMMENT 'unfollow',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章数据统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_article_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for ad_channel_label
-- ----------------------------
DROP TABLE IF EXISTS `ad_channel_label`;
CREATE TABLE `ad_channel_label`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_id` int(11) UNSIGNED NULL DEFAULT NULL,
  `label_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '标签ID',
  `ord` int(5) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '频道标签信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_channel_label
-- ----------------------------
INSERT INTO `ad_channel_label` VALUES (1108, 1, 1, 0);
INSERT INTO `ad_channel_label` VALUES (1109, 1, 8, 0);
INSERT INTO `ad_channel_label` VALUES (1110, 7, 7, 0);
INSERT INTO `ad_channel_label` VALUES (1111, 7, 9, 0);
INSERT INTO `ad_channel_label` VALUES (1112, 3, 3, 0);
INSERT INTO `ad_channel_label` VALUES (1113, 7, 2, 0);
INSERT INTO `ad_channel_label` VALUES (1114, 3, 4, 0);
INSERT INTO `ad_channel_label` VALUES (1115, 5, 5, 0);
INSERT INTO `ad_channel_label` VALUES (1116, 4, 6, 0);
INSERT INTO `ad_channel_label` VALUES (1117, 6, 10, 0);

-- ----------------------------
-- Table structure for ad_function
-- ----------------------------
DROP TABLE IF EXISTS `ad_function`;
CREATE TABLE `ad_function`  (
  `id` int(11) UNSIGNED NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '功能名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '功能代码',
  `parent_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '父功能',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '页面功能信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_function
-- ----------------------------

-- ----------------------------
-- Table structure for ad_label
-- ----------------------------
DROP TABLE IF EXISTS `ad_label`;
CREATE TABLE `ad_label`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '频道名称',
  `type` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '0系统增加\r\n            1人工增加',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24237 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '标签信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_label
-- ----------------------------
INSERT INTO `ad_label` VALUES (1, 'java', 0, '2019-08-22 16:30:16');
INSERT INTO `ad_label` VALUES (2, 'docker', 0, '2019-08-22 16:30:52');
INSERT INTO `ad_label` VALUES (3, 'mysql', 0, '2019-08-22 16:31:17');
INSERT INTO `ad_label` VALUES (4, 'vue', 0, '2019-08-22 16:35:39');
INSERT INTO `ad_label` VALUES (5, 'Weex', 0, '2019-08-22 16:36:08');
INSERT INTO `ad_label` VALUES (6, 'Python', 0, '2019-08-22 16:36:34');
INSERT INTO `ad_label` VALUES (7, '大数据', 0, '2019-08-22 16:36:48');
INSERT INTO `ad_label` VALUES (8, 'spring', 0, '2019-08-22 16:39:11');
INSERT INTO `ad_label` VALUES (9, 'hbase', 0, '2019-08-22 16:39:35');
INSERT INTO `ad_label` VALUES (10, 'hive', 0, '2019-08-22 16:43:44');
INSERT INTO `ad_label` VALUES (23858, '工具', 1, '2019-08-22 16:43:55');
INSERT INTO `ad_label` VALUES (23859, 'c', 1, '2019-08-22 16:44:01');
INSERT INTO `ad_label` VALUES (23860, '电话', 1, '2019-08-22 16:44:22');
INSERT INTO `ad_label` VALUES (23861, '照片', 1, '2019-08-22 16:44:22');
INSERT INTO `ad_label` VALUES (23862, '咨询', 1, '2019-08-22 16:44:22');
INSERT INTO `ad_label` VALUES (23863, '智能', 1, '2019-08-22 16:46:00');
INSERT INTO `ad_label` VALUES (23864, '人工智能', 1, '2019-08-22 16:46:00');
INSERT INTO `ad_label` VALUES (23865, 'AI', 1, '2019-08-22 16:46:00');
INSERT INTO `ad_label` VALUES (23866, '智能协会', 1, '2019-08-22 16:46:00');
INSERT INTO `ad_label` VALUES (23867, '人工智能协会', 1, '2019-08-22 16:46:00');
INSERT INTO `ad_label` VALUES (23868, '5G', 1, '2019-08-22 16:47:21');
INSERT INTO `ad_label` VALUES (23869, '高通', 1, '2019-08-22 16:47:21');
INSERT INTO `ad_label` VALUES (23870, '苹果', 1, '2019-08-22 16:47:21');
INSERT INTO `ad_label` VALUES (23871, '英特尔', 1, '2019-08-22 16:47:21');
INSERT INTO `ad_label` VALUES (23872, '智能手机', 1, '2019-08-22 16:47:21');
INSERT INTO `ad_label` VALUES (23873, 'google', 1, '2019-08-22 16:47:49');
INSERT INTO `ad_label` VALUES (23874, '芯片', 1, '2019-08-22 16:47:49');
INSERT INTO `ad_label` VALUES (23875, '交互', 1, '2019-08-22 16:47:49');
INSERT INTO `ad_label` VALUES (23876, '智能交互', 1, '2019-08-22 16:47:49');
INSERT INTO `ad_label` VALUES (23877, '手势交互', 1, '2019-08-22 16:47:49');
INSERT INTO `ad_label` VALUES (23878, '智慧酒店', 1, '2019-08-22 16:48:54');
INSERT INTO `ad_label` VALUES (23879, '人脸识别', 1, '2019-08-22 16:49:06');
INSERT INTO `ad_label` VALUES (23880, '软硬件开发流程', 1, '2019-08-22 16:49:26');
INSERT INTO `ad_label` VALUES (23881, '软硬件结合', 1, '2019-08-22 16:50:23');
INSERT INTO `ad_label` VALUES (23882, 'React', 1, '2019-08-22 17:18:59');
INSERT INTO `ad_label` VALUES (23883, '前端技术', 1, '2019-08-22 17:18:59');
INSERT INTO `ad_label` VALUES (23903, 'IDEA', 1, '2019-08-22 17:22:21');
INSERT INTO `ad_label` VALUES (23904, 'git', 1, '2019-08-22 17:32:54');
INSERT INTO `ad_label` VALUES (23905, 'java学习', 1, '2019-08-22 17:43:34');
INSERT INTO `ad_label` VALUES (23906, '机器学习', 1, '2019-08-22 17:46:40');
INSERT INTO `ad_label` VALUES (23907, '陆奇', 1, '2019-08-22 17:46:55');
INSERT INTO `ad_label` VALUES (23908, '设计模式', 1, '2019-08-22 17:47:40');
INSERT INTO `ad_label` VALUES (23909, '谷歌', 1, '2019-08-22 17:48:11');
INSERT INTO `ad_label` VALUES (23916, '工业数字化', 1, '2019-08-22 17:49:16');
INSERT INTO `ad_label` VALUES (23949, '个人生活', 1, '2019-08-22 17:57:38');
INSERT INTO `ad_label` VALUES (23950, '安徽', 1, '2019-08-22 17:57:38');
INSERT INTO `ad_label` VALUES (23951, '黄山', 1, '2019-08-22 17:57:38');
INSERT INTO `ad_label` VALUES (23952, 'SVN信息', 1, '2019-08-22 18:00:27');
INSERT INTO `ad_label` VALUES (23953, '清除', 1, '2019-08-22 18:00:27');
INSERT INTO `ad_label` VALUES (23954, '删除', 1, '2019-08-22 18:00:27');
INSERT INTO `ad_label` VALUES (23955, 'svn', 1, '2019-08-22 18:00:27');
INSERT INTO `ad_label` VALUES (23956, '排序', 1, '2019-08-22 18:01:27');
INSERT INTO `ad_label` VALUES (23957, '快排', 1, '2019-08-22 18:01:27');
INSERT INTO `ad_label` VALUES (23958, '归并排序', 1, '2019-08-22 18:01:27');
INSERT INTO `ad_label` VALUES (23959, '程序人生', 1, '2019-08-22 18:05:25');
INSERT INTO `ad_label` VALUES (23960, '毕业一年', 1, '2019-08-22 18:05:25');
INSERT INTO `ad_label` VALUES (23961, '毕业总结', 1, '2019-08-22 18:05:25');
INSERT INTO `ad_label` VALUES (23962, '毕业感想', 1, '2019-08-22 18:05:25');
INSERT INTO `ad_label` VALUES (23979, 'dom', 1, '2019-08-22 18:09:39');
INSERT INTO `ad_label` VALUES (23980, 'javascript', 1, '2019-08-22 18:09:39');
INSERT INTO `ad_label` VALUES (23981, 'web开发', 1, '2019-08-22 18:09:39');
INSERT INTO `ad_label` VALUES (23995, 'IT', 1, '2019-08-22 18:13:50');
INSERT INTO `ad_label` VALUES (23996, '科技', 1, '2019-08-22 18:13:50');
INSERT INTO `ad_label` VALUES (23998, '自动化', 1, '2019-08-22 18:16:20');
INSERT INTO `ad_label` VALUES (23999, '运维', 1, '2019-08-22 18:16:20');
INSERT INTO `ad_label` VALUES (24000, 'ansible', 1, '2019-08-22 18:16:20');
INSERT INTO `ad_label` VALUES (24001, '入门', 1, '2019-08-22 18:16:20');
INSERT INTO `ad_label` VALUES (24002, 'c#', 1, '2019-08-22 18:16:45');
INSERT INTO `ad_label` VALUES (24003, '正则', 1, '2019-08-22 18:16:45');
INSERT INTO `ad_label` VALUES (24004, 'regex', 1, '2019-08-22 18:16:45');
INSERT INTO `ad_label` VALUES (24005, '字符串', 1, '2019-08-22 18:16:45');
INSERT INTO `ad_label` VALUES (24015, 'openstack', 1, '2019-08-22 18:19:42');
INSERT INTO `ad_label` VALUES (24016, 'k8s', 1, '2019-08-22 18:20:47');
INSERT INTO `ad_label` VALUES (24017, 'kubernetes', 1, '2019-08-22 18:20:47');
INSERT INTO `ad_label` VALUES (24018, 'kubectl', 1, '2019-08-22 18:20:47');
INSERT INTO `ad_label` VALUES (24019, 'supervisor', 1, '2019-08-22 18:24:43');
INSERT INTO `ad_label` VALUES (24020, 'linux', 1, '2019-08-22 18:24:43');
INSERT INTO `ad_label` VALUES (24021, '进程管理', 1, '2019-08-22 18:24:43');
INSERT INTO `ad_label` VALUES (24109, '百度网盘', 1, '2019-08-22 19:14:19');
INSERT INTO `ad_label` VALUES (24110, '百度云', 1, '2019-08-22 19:14:19');
INSERT INTO `ad_label` VALUES (24111, '免费', 1, '2019-08-22 19:14:19');
INSERT INTO `ad_label` VALUES (24112, '不限速', 1, '2019-08-22 19:14:19');
INSERT INTO `ad_label` VALUES (24113, '破解', 1, '2019-08-22 19:14:19');
INSERT INTO `ad_label` VALUES (24123, '脚本', 1, '2019-08-22 19:14:26');
INSERT INTO `ad_label` VALUES (24124, '作业', 1, '2019-08-22 19:14:26');
INSERT INTO `ad_label` VALUES (24125, '自动化运维', 1, '2019-08-22 19:14:26');
INSERT INTO `ad_label` VALUES (24126, '极客头条', 1, '2019-08-22 20:34:33');
INSERT INTO `ad_label` VALUES (24198, '业界新闻', 1, '2019-08-23 07:28:27');
INSERT INTO `ad_label` VALUES (24206, '杂谈', 1, '2019-08-23 07:31:09');
INSERT INTO `ad_label` VALUES (24207, '零信任网络', 1, '2019-08-23 07:33:37');
INSERT INTO `ad_label` VALUES (24208, '网络安全', 1, '2019-08-23 07:33:37');
INSERT INTO `ad_label` VALUES (24209, 'Go语言', 1, '2019-08-23 07:33:54');
INSERT INTO `ad_label` VALUES (24210, '程序员', 1, '2019-08-23 07:33:54');
INSERT INTO `ad_label` VALUES (24223, 'Android', 1, '2019-08-23 07:35:42');
INSERT INTO `ad_label` VALUES (24224, '架构', 1, '2019-08-23 07:35:42');
INSERT INTO `ad_label` VALUES (24232, 'flume', 1, '2019-08-23 07:37:05');
INSERT INTO `ad_label` VALUES (24233, 'hive优化', 1, '2019-08-23 07:37:27');
INSERT INTO `ad_label` VALUES (24234, 'parquet', 1, '2019-08-23 07:37:27');
INSERT INTO `ad_label` VALUES (24235, 'orc', 1, '2019-08-23 07:37:27');
INSERT INTO `ad_label` VALUES (24236, 'snappy', 1, '2019-08-23 07:37:27');

-- ----------------------------
-- Table structure for ad_menu
-- ----------------------------
DROP TABLE IF EXISTS `ad_menu`;
CREATE TABLE `ad_menu`  (
  `id` int(11) UNSIGNED NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单代码',
  `parent_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '父菜单',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单资源信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_menu
-- ----------------------------

-- ----------------------------
-- Table structure for ad_recommend_strategy
-- ----------------------------
DROP TABLE IF EXISTS `ad_recommend_strategy`;
CREATE TABLE `ad_recommend_strategy`  (
  `id` int(5) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略描述',
  `is_enable` tinyint(1) NULL DEFAULT NULL COMMENT '是否有效',
  `group_id` int(5) UNSIGNED NULL DEFAULT NULL COMMENT '分组ID',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '推荐策略信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_recommend_strategy
-- ----------------------------

-- ----------------------------
-- Table structure for ad_role
-- ----------------------------
DROP TABLE IF EXISTS `ad_role`;
CREATE TABLE `ad_role`  (
  `id` int(11) UNSIGNED NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_enable` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否有效',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_role
-- ----------------------------

-- ----------------------------
-- Table structure for ad_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `ad_role_auth`;
CREATE TABLE `ad_role_auth`  (
  `id` int(11) UNSIGNED NOT NULL,
  `role_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '角色ID',
  `type` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '资源类型\r\n            0 菜单\r\n            1 功能',
  `entry_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '资源ID',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_role_auth
-- ----------------------------

-- ----------------------------
-- Table structure for ad_strategy_group
-- ----------------------------
DROP TABLE IF EXISTS `ad_strategy_group`;
CREATE TABLE `ad_strategy_group`  (
  `id` int(5) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略描述',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '推荐策略分组信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_strategy_group
-- ----------------------------

-- ----------------------------
-- Table structure for ad_user
-- ----------------------------
DROP TABLE IF EXISTS `ad_user`;
CREATE TABLE `ad_user`  (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `password` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '盐',
  `nickname` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(11) UNSIGNED NULL DEFAULT NULL COMMENT '状态\r\n            0 暂时不可用\r\n            1 永久不可用\r\n            9 正常可用',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次登录时间',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_user
-- ----------------------------
INSERT INTO `ad_user` VALUES (1, 'wukong', '123', NULL, 'mo', NULL, '13320325525', 1, 'wukong@qq.com', '2019-07-30 10:16:41', '2019-07-30 10:16:43');
INSERT INTO `ad_user` VALUES (2, 'admin', '5d4e1a406d4a9edbf7b4f10c2a390405', '123abc', 'ad', NULL, '13320325528', 1, 'admin@qq.com', '2020-03-04 17:07:37', '2020-03-04 17:07:40');
INSERT INTO `ad_user` VALUES (3, 'guest', '34e20b52f5bd120db806e57e27f47ed0', '123456', 'gu', NULL, '13412345676', 1, 'guest@qq.com', '2020-07-30 15:00:03', '2020-07-30 15:00:06');

-- ----------------------------
-- Table structure for ad_user_equipment
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_equipment`;
CREATE TABLE `ad_user_equipment`  (
  `id` int(11) UNSIGNED NOT NULL,
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `type` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '0PC\r\n            1ANDROID\r\n            2IOS\r\n            3PAD\r\n            9 其他',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备版本',
  `sys` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备系统',
  `no` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备唯一号，MD5加密',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员设备信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_user_equipment
-- ----------------------------

-- ----------------------------
-- Table structure for ad_user_login
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_login`;
CREATE TABLE `ad_user_login`  (
  `id` int(11) UNSIGNED NOT NULL,
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `equipment_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '登录设备ID',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP',
  `address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地址',
  `longitude` decimal(5, 5) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(5, 5) NULL DEFAULT NULL COMMENT '纬度',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员登录行为信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_user_login
-- ----------------------------

-- ----------------------------
-- Table structure for ad_user_opertion
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_opertion`;
CREATE TABLE `ad_user_opertion`  (
  `id` int(11) UNSIGNED NOT NULL,
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `equipment_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '登录设备ID',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP',
  `address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地址',
  `type` int(5) NULL DEFAULT NULL COMMENT '操作类型',
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作描述',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员操作行为信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_user_opertion
-- ----------------------------

-- ----------------------------
-- Table structure for ad_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ad_user_role`;
CREATE TABLE `ad_user_role`  (
  `id` int(11) UNSIGNED NOT NULL,
  `role_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for ad_vistor_statistics
-- ----------------------------
DROP TABLE IF EXISTS `ad_vistor_statistics`;
CREATE TABLE `ad_vistor_statistics`  (
  `id` int(11) UNSIGNED NOT NULL COMMENT '主键',
  `activity` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '日活',
  `vistor` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '访问量',
  `ip` int(11) UNSIGNED NULL DEFAULT NULL COMMENT 'IP量',
  `register` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '注册量',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '访问数据统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ad_vistor_statistics
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
