/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : leadnews_wemedia

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2021-10-13 17:12:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for wm_channel
-- ----------------------------
DROP TABLE IF EXISTS `wm_channel`;
CREATE TABLE `wm_channel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道描述',
  `is_default` tinyint(1) unsigned DEFAULT NULL COMMENT '是否默认频道',
  `status` tinyint(1) unsigned DEFAULT NULL,
  `ord` tinyint(3) unsigned DEFAULT NULL COMMENT '默认排序',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='频道信息表';

-- ----------------------------
-- Records of wm_channel
-- ----------------------------
INSERT INTO `wm_channel` VALUES ('0', '其他', '其他', '1', '1', '12', '2020-09-11 15:09:30');
INSERT INTO `wm_channel` VALUES ('1', 'java', '后端框架', '1', '1', '1', '2020-10-13 12:25:30');
INSERT INTO `wm_channel` VALUES ('2', 'Mysql', '轻量级数据库', '1', '1', '4', '2019-08-16 10:55:41');
INSERT INTO `wm_channel` VALUES ('3', 'Vue', '阿里前端框架', '1', '1', '5', '2019-08-16 10:55:45');
INSERT INTO `wm_channel` VALUES ('4', 'Python', '未来的语言', '1', '1', '6', '2019-08-05 17:33:17');
INSERT INTO `wm_channel` VALUES ('5', 'Weex', '向未来致敬', '1', '1', '7', '2019-08-16 10:56:26');
INSERT INTO `wm_channel` VALUES ('6', '大数据', '不会，则不要说自己是搞互联网的', '1', '1', '10', '2020-09-11 15:06:11');
