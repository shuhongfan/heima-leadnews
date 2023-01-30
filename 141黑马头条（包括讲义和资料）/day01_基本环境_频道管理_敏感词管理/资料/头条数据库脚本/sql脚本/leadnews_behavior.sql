CREATE DATABASE IF NOT EXISTS leadnews_behavior DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE leadnews_behavior;
SET NAMES utf8;
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : leadnews_behavior

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-10-09 18:26:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ap_behavior_entry
-- ----------------------------
DROP TABLE IF EXISTS `ap_behavior_entry`;
CREATE TABLE `ap_behavior_entry` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '实体类型\r\n            0终端设备\r\n            1用户',
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP行为实体表,一个行为实体可能是用户或者设备，或者其它';

-- ----------------------------
-- Records of ap_behavior_entry
-- ----------------------------
INSERT INTO `ap_behavior_entry` VALUES ('1', '1', '4', '2020-04-07 22:17:22');
INSERT INTO `ap_behavior_entry` VALUES ('2', '0', '88888888', '2020-09-29 17:09:00');

-- ----------------------------
-- Table structure for ap_follow_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ap_follow_behavior`;
CREATE TABLE `ap_follow_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `follow_id` int(11) unsigned DEFAULT NULL COMMENT '关注用户ID',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_entry_id` (`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP关注行为表';

-- ----------------------------
-- Records of ap_follow_behavior
-- ----------------------------
INSERT INTO `ap_follow_behavior` VALUES ('1306493322981928961', '1', '1303156149041758210', '4', '2020-09-17 15:20:56');
INSERT INTO `ap_follow_behavior` VALUES ('1306493386580160514', '1', '1303156149041758210', '4', '2020-09-17 15:21:11');

-- ----------------------------
-- Table structure for ap_forward_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ap_forward_behavior`;
CREATE TABLE `ap_forward_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `dynamic_id` int(11) unsigned DEFAULT NULL,
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_entry_id` (`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP转发行为表';

-- ----------------------------
-- Records of ap_forward_behavior
-- ----------------------------

-- ----------------------------
-- Table structure for ap_likes_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ap_likes_behavior`;
CREATE TABLE `ap_likes_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '点赞内容类型\r\n            0文章\r\n            1动态',
  `operation` tinyint(1) unsigned DEFAULT NULL COMMENT '0 点赞\r\n            1 取消点赞',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP点赞行为表';

-- ----------------------------
-- Records of ap_likes_behavior
-- ----------------------------
INSERT INTO `ap_likes_behavior` VALUES ('1306556220991197185', '1', '1303156149041758210', '0', '0', '2020-09-17 19:30:50');
INSERT INTO `ap_likes_behavior` VALUES ('1306826584845467649', '1', '1302865474094120961', '0', '0', '2020-09-18 13:25:12');

-- ----------------------------
-- Table structure for ap_read_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ap_read_behavior`;
CREATE TABLE `ap_read_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `count` tinyint(3) unsigned DEFAULT NULL,
  `read_duration` int(11) unsigned DEFAULT NULL COMMENT '阅读时间单位秒',
  `percentage` tinyint(3) unsigned DEFAULT NULL COMMENT '阅读百分比',
  `load_duration` int(11) unsigned DEFAULT NULL COMMENT '文章加载时间',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP阅读行为表';

-- ----------------------------
-- Records of ap_read_behavior
-- ----------------------------
INSERT INTO `ap_read_behavior` VALUES ('1306791787066572802', '1', '1302977178887004162', '2', '9200', '99', '0', '2020-09-18 11:06:55', '2020-09-18 11:07:37');
INSERT INTO `ap_read_behavior` VALUES ('1306820004724883458', '1', '1303156149041758210', '40', '5600', '99', '0', '2020-09-18 12:59:03', '2020-09-27 19:09:56');
INSERT INTO `ap_read_behavior` VALUES ('1306826540398428161', '1', '1302977754114826243', '1', '1600', '0', '1600', '2020-09-18 13:25:02', null);
INSERT INTO `ap_read_behavior` VALUES ('1306826607201107970', '1', '1302865474094120961', '1', '13000', '97', '0', '2020-09-18 13:25:17', null);
INSERT INTO `ap_read_behavior` VALUES ('1309389244548116482', '1', '1302977754114826241', '1', '2500', '99', '0', '2020-09-25 15:08:17', null);
INSERT INTO `ap_read_behavior` VALUES ('1314024363527266305', '1', '1302865008438296600', '1', '1900', '0', '1900', '2020-10-08 10:06:36', null);
INSERT INTO `ap_read_behavior` VALUES ('1314050329599303681', '1', '1302865306489733122', '1', '1700', '0', '100', '2020-10-08 11:49:47', null);
INSERT INTO `ap_read_behavior` VALUES ('1314050541399072769', '1', '1302862387124125698', '1', '44900', '99', '0', '2020-10-08 11:50:38', null);

-- ----------------------------
-- Table structure for ap_share_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ap_share_behavior`;
CREATE TABLE `ap_share_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `type` tinyint(2) unsigned DEFAULT NULL COMMENT '0 微信\r\n            1 微信朋友圈\r\n            2 QQ\r\n            3 QQ 空间\r\n            4 微博\r\n            ',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_entry_id` (`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP分享行为表';

-- ----------------------------
-- Records of ap_share_behavior
-- ----------------------------

-- ----------------------------
-- Table structure for ap_show_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ap_show_behavior`;
CREATE TABLE `ap_show_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `is_click` tinyint(1) unsigned DEFAULT NULL COMMENT '是否点击',
  `show_time` datetime DEFAULT NULL COMMENT '文章加载时间',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_entry_id` (`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP文章展现行为表';

-- ----------------------------
-- Records of ap_show_behavior
-- ----------------------------

-- ----------------------------
-- Table structure for ap_unlikes_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ap_unlikes_behavior`;
CREATE TABLE `ap_unlikes_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `type` tinyint(2) unsigned DEFAULT NULL COMMENT '0 不喜欢\r\n            1 取消不喜欢',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP不喜欢行为表';

-- ----------------------------
-- Records of ap_unlikes_behavior
-- ----------------------------
INSERT INTO `ap_unlikes_behavior` VALUES ('11222', '1', '1303156149041758210', '1', '2020-04-07 23:27:23');
