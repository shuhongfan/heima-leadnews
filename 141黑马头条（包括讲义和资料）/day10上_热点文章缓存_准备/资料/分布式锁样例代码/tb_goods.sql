/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 60011
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 60011
File Encoding         : 65001

Date: 2019-11-20 15:15:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods`;
CREATE TABLE `tb_goods` (
  `goods_code` varchar(255) DEFAULT NULL,
  `goods_num` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_goods
-- ----------------------------
INSERT INTO `tb_goods` VALUES ('banala', '234');
INSERT INTO `tb_goods` VALUES ('dress', '356789');
INSERT INTO `tb_goods` VALUES ('shirt', '2334');
INSERT INTO `tb_goods` VALUES ('apple', '0');
