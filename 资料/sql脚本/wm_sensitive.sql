/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : leadnews_wemedia

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2021-05-23 11:19:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ad_sensitive
-- ----------------------------
DROP TABLE IF EXISTS `wm_sensitive`;
CREATE TABLE `wm_sensitive` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sensitives` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '敏感词',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='敏感词信息表';

-- ----------------------------
-- Records of wm_sensitive
-- ----------------------------
INSERT INTO `wm_sensitive` VALUES ('3104', '冰毒', '2021-05-23 15:38:51');
INSERT INTO `wm_sensitive` VALUES ('3105', '法轮功', '2021-05-23 15:38:51');
INSERT INTO `wm_sensitive` VALUES ('3106', '私人侦探', '2021-05-23 11:09:22');
INSERT INTO `wm_sensitive` VALUES ('3107', '针孔摄象', '2021-05-23 11:09:52');
INSERT INTO `wm_sensitive` VALUES ('3108', '信用卡提现', '2021-05-23 11:10:11');
INSERT INTO `wm_sensitive` VALUES ('3109', '无抵押贷款', '2021-05-23 11:10:41');
INSERT INTO `wm_sensitive` VALUES ('3110', '广告代理', '2021-05-23 11:10:59');
INSERT INTO `wm_sensitive` VALUES ('3111', '代开发票', '2021-05-23 11:11:18');
INSERT INTO `wm_sensitive` VALUES ('3112', '蚁力神', '2021-05-23 11:11:39');
INSERT INTO `wm_sensitive` VALUES ('3113', '售肾', '2021-05-23 11:12:08');
INSERT INTO `wm_sensitive` VALUES ('3114', '刻章办', '2021-05-23 11:12:24');
INSERT INTO `wm_sensitive` VALUES ('3116', '套牌车', '2021-05-23 11:12:37');
INSERT INTO `wm_sensitive` VALUES ('3117', '足球投注', '2021-05-23 11:12:51');
INSERT INTO `wm_sensitive` VALUES ('3118', '地下钱庄', '2021-05-23 11:13:07');
INSERT INTO `wm_sensitive` VALUES ('3119', '出售答案', '2021-05-23 11:13:24');
INSERT INTO `wm_sensitive` VALUES ('3200', '小额贷款', '2021-05-23 11:13:40');
