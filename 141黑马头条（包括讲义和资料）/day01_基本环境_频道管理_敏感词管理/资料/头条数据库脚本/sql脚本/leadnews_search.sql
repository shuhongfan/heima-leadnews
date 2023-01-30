CREATE DATABASE IF NOT EXISTS leadnews_search DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE leadnews_search;
SET NAMES utf8;
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : leadnews_search

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-10-09 18:26:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ap_associate_words
-- ----------------------------
DROP TABLE IF EXISTS `ap_associate_words`;
CREATE TABLE `ap_associate_words` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `associate_words` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联想词',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_associate_words` (`associate_words`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='联想词表';

-- ----------------------------
-- Records of ap_associate_words
-- ----------------------------
INSERT INTO `ap_associate_words` VALUES ('1', '黑马程序员', '2020-09-29 14:07:29');
INSERT INTO `ap_associate_words` VALUES ('2', '黑马头条', '2020-09-29 14:15:58');
INSERT INTO `ap_associate_words` VALUES ('3', '黑马旅游', '2020-09-29 14:16:06');

-- ----------------------------
-- Table structure for ap_hot_words
-- ----------------------------
DROP TABLE IF EXISTS `ap_hot_words`;
CREATE TABLE `ap_hot_words` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `hot_words` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '热词',
  `type` tinyint(2) DEFAULT NULL COMMENT '0:热,1:荐,2:新,3:火,4:精,5:亮',
  `hot_date` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '热词日期',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_hot_date` (`hot_date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='搜索热词表';

-- ----------------------------
-- Records of ap_hot_words
-- ----------------------------
INSERT INTO `ap_hot_words` VALUES ('1', '黑马程序员', '0', '2020-04-20', '2020-04-20 14:08:06');
INSERT INTO `ap_hot_words` VALUES ('2', '黑马', '0', '2020-04-20', '2020-04-20 14:34:27');

-- ----------------------------
-- Table structure for ap_user_search
-- ----------------------------
DROP TABLE IF EXISTS `ap_user_search`;
CREATE TABLE `ap_user_search` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `keyword` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '搜索词',
  `status` tinyint(2) unsigned DEFAULT NULL COMMENT '当前状态0 无效 1有效',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户搜索信息表';

-- ----------------------------
-- Records of ap_user_search
-- ----------------------------
INSERT INTO `ap_user_search` VALUES ('18', '1', 'java', '1', '2020-10-08 14:27:22');
INSERT INTO `ap_user_search` VALUES ('19', '1', '测试', '0', '2020-10-08 14:27:42');
INSERT INTO `ap_user_search` VALUES ('20', '1', '黑马头条', '1', '2020-10-08 18:02:27');
INSERT INTO `ap_user_search` VALUES ('21', '1', '黑马程序员马', '1', '2020-10-08 18:29:21');
