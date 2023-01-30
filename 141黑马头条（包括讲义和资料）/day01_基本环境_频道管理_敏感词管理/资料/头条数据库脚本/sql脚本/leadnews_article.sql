CREATE DATABASE IF NOT EXISTS leadnews_article DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE leadnews_article;
SET NAMES utf8;
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : leadnews_article

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-10-09 18:26:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ap_article
-- ----------------------------
DROP TABLE IF EXISTS `ap_article`;
CREATE TABLE `ap_article` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `author_id` int(11) unsigned DEFAULT NULL COMMENT '文章作者的ID',
  `author_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者昵称',
  `channel_id` int(10) unsigned DEFAULT NULL COMMENT '文章所属频道ID',
  `channel_name` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道名称',
  `layout` tinyint(1) unsigned DEFAULT NULL COMMENT '文章布局\r\n            0 无图文章\r\n            1 单图文章\r\n            2 多图文章',
  `flag` tinyint(3) unsigned DEFAULT NULL COMMENT '文章标记\r\n            0 普通文章\r\n            1 热点文章\r\n            2 置顶文章\r\n            3 精品文章\r\n            4 大V 文章',
  `images` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章图片\r\n            多张逗号分隔',
  `labels` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章标签最多3个 逗号分隔',
  `likes` int(5) unsigned DEFAULT NULL COMMENT '点赞数量',
  `collection` int(5) unsigned DEFAULT NULL COMMENT '收藏数量',
  `comment` int(5) unsigned DEFAULT NULL COMMENT '评论数量',
  `views` int(5) unsigned DEFAULT NULL COMMENT '阅读数量',
  `province_id` int(11) unsigned DEFAULT NULL COMMENT '省市',
  `city_id` int(11) unsigned DEFAULT NULL COMMENT '市区',
  `county_id` int(11) unsigned DEFAULT NULL COMMENT '区县',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `sync_status` tinyint(1) DEFAULT '0' COMMENT '同步状态',
  `origin` tinyint(1) unsigned DEFAULT '0' COMMENT '来源',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1303156149041758211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='文章信息表，存储已发布的文章';

-- ----------------------------
-- Records of ap_article
-- ----------------------------
INSERT INTO `ap_article` VALUES ('1302862387124125698', '什么是Java语言', '4', 'admin', '1', 'java', '1', null, 'group1/M00/00/00/wKjIgl9V2CqAZe18AAOoOOsvWPc041.png', null, null, null, null, null, null, null, null, '2020-09-07 14:52:54', '2020-09-07 14:56:18', '0', '0');
INSERT INTO `ap_article` VALUES ('1302864436297482242', 'Java语言跨平台原理', '4', 'admin', '1', 'java', '1', null, 'group1/M00/00/00/wKjIgl9V2n6AArZsAAGMmaPdt7w502.png', null, null, null, null, null, null, null, null, '2020-09-07 15:01:02', '2020-09-07 15:01:02', '0', '0');
INSERT INTO `ap_article` VALUES ('1302864730402078722', '我是一个测试标题', '4', 'admin', '1', 'java', '1', null, 'group1/M00/00/00/wKjIgl892wqAANwOAAJW8oQUlAc087.jpg', null, null, null, null, null, null, null, null, '2020-09-07 15:02:12', '2020-09-07 15:02:12', '0', '0');
INSERT INTO `ap_article` VALUES ('1302865008438296577', '过山车故障20名游客倒挂空中', '4', 'admin', '1', 'java', '3', null, 'group1/M00/00/00/wKjIgl892wqAANwOAAJW8oQUlAc087.jpg,group1/M00/00/00/wKjIgl892xmAG_yjAAB6OkkuJd4819.jpg,group1/M00/00/00/wKjIgl892wKAZLhtAASZUi49De0836.jpg', null, null, null, null, null, null, null, null, '2020-09-07 15:03:19', '2020-09-07 15:03:19', '0', '0');
INSERT INTO `ap_article` VALUES ('1302865306489733122', '武汉高校开学典礼万人歌唱祖国', '4', 'admin', '1', 'java', '3', null, 'group1/M00/00/00/wKjIgl892vuAXr_MAASCMYD0yzc919.jpg,group1/M00/00/00/wKjIgl892xGANV6qAABzWOH8KDY775.jpg,group1/M00/00/00/wKjIgl892wqAANwOAAJW8oQUlAc087.jpg', null, null, null, null, null, null, null, null, '2020-09-07 15:04:30', '2020-09-07 15:04:30', '0', '0');
INSERT INTO `ap_article` VALUES ('1302865474094120961', '天降铁球砸死女婴整栋楼被判赔', '4', 'admin', '1', 'java', '1', null, 'group1/M00/00/00/wKjIgl892tyAFc60AAMUNUuOKPA619.jpg', null, null, null, null, null, null, null, null, '2020-09-07 15:05:10', '2020-09-07 15:05:10', '0', '0');
INSERT INTO `ap_article` VALUES ('1302977178887004162', '央视曝光境外医疗豪华旅游套路', '4', 'admin', '1', 'java', '0', null, 'group1/M00/00/00/wKjIgl892wqAANwOAAJW8oQUlAc087.jpg', null, null, null, null, null, null, null, null, '2020-09-07 22:29:02', '2020-09-07 22:29:02', '0', '0');
INSERT INTO `ap_article` VALUES ('1302977458215067649', '10多名陌生人合力托举悬窗女童', '4', 'admin', '1', 'java', '1', null, 'group1/M00/00/00/wKjIgl892vOASiunAAGzs3UZ1Cg252.jpg', null, null, null, null, null, null, null, null, '2020-09-07 22:30:09', '2020-09-07 22:30:09', '0', '0');
INSERT INTO `ap_article` VALUES ('1302977558807060482', '杨澜回应一秒变脸', '4', 'admin', '1', 'java', '1', null, 'group1/M00/00/00/wKjIgl892wKAZLhtAASZUi49De0836.jpg', null, null, null, null, null, null, null, null, '2020-09-07 22:30:33', '2020-09-07 22:30:33', '0', '0');
INSERT INTO `ap_article` VALUES ('1302977754114826241', '黄龄工作室发视频回应', '4', 'admin', '4', 'Python', '1', null, 'group1/M00/00/00/wKjIgl892vuAXr_MAASCMYD0yzc919.jpg', null, null, null, null, null, null, null, null, '2020-09-07 22:31:19', '2020-09-07 22:31:19', '0', '0');
INSERT INTO `ap_article` VALUES ('1302977754114826242', '黄龄工作室发视频回应', '4', 'admin', '4', 'Python', '1', null, 'group1/M00/00/00/wKjIgl892vuAXr_MAASCMYD0yzc919.jpg', '', null, null, null, null, null, null, null, '2020-09-07 22:31:19', '2020-09-07 22:31:19', '0', '0');
INSERT INTO `ap_article` VALUES ('1302977754114826243', '黄龄工作室发视频回应', '4', 'admin', '4', 'Python', '1', null, 'group1/M00/00/00/wKjIgl892vuAXr_MAASCMYD0yzc919.jpg', '', null, null, null, null, null, null, null, '2020-09-07 22:31:19', '2020-09-07 22:31:19', '0', '0');
INSERT INTO `ap_article` VALUES ('1303156149041758210', '全国抗击新冠肺炎疫情表彰大会', '4', 'admin', '1', 'java', '1', null, 'group1/M00/00/00/wKjIgl9W6iOAD2doAAFY4E1K7-g384.png', null, null, null, null, null, null, null, null, '2020-09-08 10:20:12', '2020-09-08 10:20:12', '0', '0');

-- ----------------------------
-- Table structure for ap_article_config
-- ----------------------------
DROP TABLE IF EXISTS `ap_article_config`;
CREATE TABLE `ap_article_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `is_comment` tinyint(1) unsigned DEFAULT NULL COMMENT '是否可评论',
  `is_forward` tinyint(1) unsigned DEFAULT NULL COMMENT '是否转发',
  `is_down` tinyint(1) unsigned DEFAULT NULL COMMENT '是否下架',
  `is_delete` tinyint(1) unsigned DEFAULT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_article_id` (`article_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1303156149909979138 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP已发布文章配置表';

-- ----------------------------
-- Records of ap_article_config
-- ----------------------------
INSERT INTO `ap_article_config` VALUES ('1302862387933626369', '1302862387124125698', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302864437425750018', '1302864436297482242', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302864731203190785', '1302864730402078722', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302865009533009922', '1302865008438296577', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302865307408285697', '1302865306489733122', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302865475297886209', '1302865474094120961', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302977180199821313', '1302977178887004162', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302977459322363905', '1302977458215067649', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302977559788527618', '1302977558807060482', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302977754882383873', '1302977754114826241', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302977754882383874', '1302977754114826242', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1302977754882383875', '1302977754114826243', '1', '1', '0', '0');
INSERT INTO `ap_article_config` VALUES ('1303156149909979137', '1303156149041758210', '1', '1', '0', '0');

-- ----------------------------
-- Table structure for ap_article_content
-- ----------------------------
DROP TABLE IF EXISTS `ap_article_content`;
CREATE TABLE `ap_article_content` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '文章内容',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_article_id` (`article_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1303156151151493122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP已发布文章内容表';

-- ----------------------------
-- Records of ap_article_content
-- ----------------------------
INSERT INTO `ap_article_content` VALUES ('1302862388957036545', '1302862387124125698', '[{\"type\":\"text\",\"value\":\"Java语言是美国Sun公司（Stanford University Network），在1995年推出的高级的编程语言。所谓编程语言，是计算机的语言，人们可以使用编程语言对计算机下达命令，让计算机完成人们需要的功能。\\n\\n2009年，Sun公司被甲骨文公司收购，所以我们现在访问oracle官网即可：https://www.oracle.com\\nJava语言共同创始人之一：詹姆斯·高斯林 （James Gosling），被称为“Java之父”\\n\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl9V2CqAZe18AAOoOOsvWPc041.png\"},{\"type\":\"text\",\"value\":\"Java语言发展历史\\n\\n- 1995年Sun公司推出Java语言\\n- 1996年发布Java 1.0版本\\n- 1997年发布Java 1.1版本\\n- 1998年发布Java 1.2版本\\n- 2000年发布Java 1.3版本\\n- 2002年发布Java 1.4版本\\n- 2004年发布Java 5.0版本\\n- 2006年发布Java 6.0版本\\n- 2009年Oracle甲骨文公司收购Sun公司\\n- 2011年发布Java 7.0版本\\n- 2014年发布Java 8.0版本\\n- 2017年9月发布Java 9.0版本\\n- 2018年3月发布Java 10.0版本\\n- 2018年9月发布Java 11.0版本\\n\"},{\"type\":\"text\",\"value\":\"Java 语言的三个版本\\n\\n- JavaSE：标准版，用于桌面应用的开发，是其他两个版本的基础。\\n  - 学习JavaSE的目的, 是为了就业班要学习的JavaEE打基础.\\n\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl9V2F6AdQxAAAGyaOdp4gk784.png\"},{\"type\":\"text\",\"value\":\"- JavaEE：企业版，用于Web方向的网站开发\\n  - 网站：网页 + 后台服务器\\n\\nJava语言主要应用在互联网程序的开发领域。常见的互联网程序比如天猫、京东、物流系统、网银系统等，以及服务器后台处理大数据的存储、查询、数据挖掘等也有很多应用。\\n\"}]');
INSERT INTO `ap_article_content` VALUES ('1302864438885367810', '1302864436297482242', '[{\"type\":\"text\",\"value\":\"Java虚拟机——JVM\\n\\n- JVM（Java Virtual Machine ）：Java虚拟机，简称JVM，是运行所有Java程序的假想计算机，是Java程序的运行环境，是Java 最具吸引力的特性之一。我们编写的Java代码，都运行在JVM 之上。\\n- 跨平台：任何软件的运行，都必须要运行在操作系统之上，而我们用Java编写的软件可以运行在任何的操作系统上，这个特性称为Java语言的跨平台特性。该特性是由JVM实现的，我们编写的程序运行在JVM上，而JVM运行在操作系统上。\\n\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl9V2n6AArZsAAGMmaPdt7w502.png\"},{\"type\":\"text\",\"value\":\"如图所示，Java的虚拟机本身不具备跨平台功能的，每个操作系统下都有不同版本的虚拟机。\\n\\n问题1: Java 是如何实现跨平台的呢？\\n\\n- 答：因为在不同操作系统中都安装了对应版本的 JVM 虚拟机\\n- 注意: Java程序想要运行, 必须依赖于JVM虚拟机.\\n\\n问题2: JVM 本身是否允许跨平台呢？\\n\\n- 答：不允许，允许跨平台的是 Java 程序，而不是虚拟机。\\n\"}]');
INSERT INTO `ap_article_content` VALUES ('1302864732679585794', '1302864730402078722', '[{\"type\":\"text\",\"value\":\"这些都是测试这些都是测试这些都是测试这些都是测试这些都是测试这些都是测试\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892wqAANwOAAJW8oQUlAc087.jpg\"},{\"type\":\"text\",\"value\":\"这些都是测试这些都是测试这些都是测试这些都是测试\"}]');
INSERT INTO `ap_article_content` VALUES ('1302865011026182145', '1302865008438296577', '[{\"type\":\"text\",\"value\":\"过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892uyAR12rAADi7UxPXeM267.jpg\"},{\"type\":\"text\",\"value\":\"过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中过山车故障20名游客倒挂空中\"},{\"type\":\"text\",\"value\":\"请在这里输入正文\"}]');
INSERT INTO `ap_article_content` VALUES ('1302865308704325633', '1302865306489733122', '[{\"type\":\"text\",\"value\":\"武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892vuAXr_MAASCMYD0yzc919.jpg\"},{\"type\":\"text\",\"value\":\"武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国武汉高校开学典礼万人歌唱祖国v\"}]');
INSERT INTO `ap_article_content` VALUES ('1302865476799447041', '1302865474094120961', '[{\"type\":\"text\",\"value\":\"天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892tyAFc60AAMUNUuOKPA619.jpg\"},{\"type\":\"text\",\"value\":\"天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔天降铁球砸死女婴整栋楼被判赔vv\"},{\"type\":\"text\",\"value\":\"请在这里输入正文\"}]');
INSERT INTO `ap_article_content` VALUES ('1302977181835599873', '1302977178887004162', '[{\"type\":\"text\",\"value\":\"央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路央视曝光境外医疗豪华旅游套路\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892wqAANwOAAJW8oQUlAc087.jpg\"}]');
INSERT INTO `ap_article_content` VALUES ('1302977460907810818', '1302977458215067649', '[{\"type\":\"text\",\"value\":\"510多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892vOASiunAAGzs3UZ1Cg252.jpg\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892uyAR12rAADi7UxPXeM267.jpg\"},{\"type\":\"text\",\"value\":\"10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童v\"},{\"type\":\"text\",\"value\":\"请10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童10多名陌生人合力托举悬窗女童v\"}]');
INSERT INTO `ap_article_content` VALUES ('1302977561034235906', '1302977558807060482', '[{\"type\":\"text\",\"value\":\"杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892wKAZLhtAASZUi49De0836.jpg\"},{\"type\":\"text\",\"value\":\"杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸\"},{\"type\":\"text\",\"value\":\"请在这里输入正文\"}]');
INSERT INTO `ap_article_content` VALUES ('1302977755742216193', '1302977754114826241', '[{\"type\":\"text\",\"value\":\"3黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892vuAXr_MAASCMYD0yzc919.jpg\"},{\"type\":\"text\",\"value\":\"请在这里输入正文黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应黄龄工作室发视频回应\"}]');
INSERT INTO `ap_article_content` VALUES ('1303156151151493121', '1303156149041758210', '[{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl9W6iOAD2doAAFY4E1K7-g384.png\"},{\"type\":\"text\",\"value\":\"全国抗击新冠肺炎疫情表彰大会开始15家“文化会客厅”展现产业发展的集群效应全球疫情简报:印度新冠确诊病例超420万 升至全球第二中方提出《全球数据安全倡议》\"}]');

-- ----------------------------
-- Table structure for ap_article_label
-- ----------------------------
DROP TABLE IF EXISTS `ap_article_label`;
CREATE TABLE `ap_article_label` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint(20) unsigned DEFAULT NULL,
  `label_id` int(11) unsigned DEFAULT NULL COMMENT '标签ID',
  `count` int(5) unsigned DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章标签信息表';

-- ----------------------------
-- Records of ap_article_label
-- ----------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='联想词表';

-- ----------------------------
-- Records of ap_associate_words
-- ----------------------------

-- ----------------------------
-- Table structure for ap_author
-- ----------------------------
DROP TABLE IF EXISTS `ap_author`;
CREATE TABLE `ap_author` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '作者名称',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '0 爬取数据\r\n            1 签约合作商\r\n            2 平台自媒体人\r\n            ',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '社交账号ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `wm_user_id` int(11) unsigned DEFAULT NULL COMMENT '自媒体账号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_type_name` (`type`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='APP文章作者信息表';

-- ----------------------------
-- Records of ap_author
-- ----------------------------
INSERT INTO `ap_author` VALUES ('1', 'zhangsan', '2', '1', '2020-03-19 23:43:54', null);
INSERT INTO `ap_author` VALUES ('2', 'lisi', '2', '2', '2020-03-19 23:47:44', null);
INSERT INTO `ap_author` VALUES ('3', 'wangwu', '2', '3', '2020-03-19 23:50:09', null);
INSERT INTO `ap_author` VALUES ('4', 'admin', '2', '4', '2020-03-30 16:36:41', null);

-- ----------------------------
-- Table structure for ap_collection
-- ----------------------------
DROP TABLE IF EXISTS `ap_collection`;
CREATE TABLE `ap_collection` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '点赞内容类型\r\n            0文章\r\n            1动态',
  `collection_time` datetime DEFAULT NULL COMMENT '创建时间',
  `published_time` datetime DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_type` (`entry_id`,`article_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP收藏信息表';

-- ----------------------------
-- Records of ap_collection
-- ----------------------------
INSERT INTO `ap_collection` VALUES ('1', '1', '1303156149041758210', '0', '2020-04-07 23:46:47', '2020-04-07 23:46:50');

-- ----------------------------
-- Table structure for ap_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `ap_dynamic`;
CREATE TABLE `ap_dynamic` (
  `id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '文章作者的ID',
  `user_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者昵称',
  `content` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道名称',
  `is_forward` tinyint(1) unsigned DEFAULT NULL COMMENT '是否转发',
  `article_id` bigint(11) unsigned DEFAULT NULL COMMENT '转发文章ID',
  `articel_title` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '转发文章标题',
  `article_image` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '转发文章图片',
  `layout` tinyint(1) unsigned DEFAULT NULL COMMENT '布局标识\r\n            0 无图动态\r\n            1 单图动态\r\n            2 多图动态\r\n            3 转发动态',
  `images` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章图片\r\n            多张逗号分隔',
  `likes` int(5) unsigned DEFAULT NULL COMMENT '点赞数量',
  `collection` int(5) unsigned DEFAULT NULL COMMENT '收藏数量',
  `comment` int(5) unsigned DEFAULT NULL COMMENT '评论数量',
  `views` int(5) unsigned DEFAULT NULL COMMENT '阅读数量',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户动态信息表';

-- ----------------------------
-- Records of ap_dynamic
-- ----------------------------

-- ----------------------------
-- Table structure for ap_equipment
-- ----------------------------
DROP TABLE IF EXISTS `ap_equipment`;
CREATE TABLE `ap_equipment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '0PC\r\n            1ANDROID\r\n            2IOS\r\n            3PAD\r\n            9 其他',
  `version` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备版本',
  `sys` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备系统',
  `no` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备唯一号，MD5加密',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP设备信息表';

-- ----------------------------
-- Records of ap_equipment
-- ----------------------------

-- ----------------------------
-- Table structure for ap_equipment_code
-- ----------------------------
DROP TABLE IF EXISTS `ap_equipment_code`;
CREATE TABLE `ap_equipment_code` (
  `id` int(11) unsigned NOT NULL,
  `equipment_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `code` longtext COLLATE utf8mb4_unicode_ci COMMENT '设备码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP设备码信息表';

-- ----------------------------
-- Records of ap_equipment_code
-- ----------------------------

-- ----------------------------
-- Table structure for ap_hot_articles
-- ----------------------------
DROP TABLE IF EXISTS `ap_hot_articles`;
CREATE TABLE `ap_hot_articles` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `entry_id` int(11) unsigned NOT NULL DEFAULT '0',
  `tag_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '频道ID',
  `tag_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道名称',
  `score` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '热度评分',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `province_id` int(11) unsigned DEFAULT NULL COMMENT '省市',
  `city_id` int(11) unsigned DEFAULT NULL COMMENT '市区',
  `county_id` int(11) unsigned DEFAULT NULL COMMENT '区县',
  `is_read` tinyint(2) unsigned DEFAULT NULL COMMENT '是否阅读',
  `release_date` datetime DEFAULT NULL,
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_entry_id` (`entry_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='热文章表';

-- ----------------------------
-- Records of ap_hot_articles
-- ----------------------------
INSERT INTO `ap_hot_articles` VALUES ('1', '0', '1', '', '181', '1', '1', '1', '1', '0', '2020-05-04 15:18:13', '2020-05-05 14:24:35');
INSERT INTO `ap_hot_articles` VALUES ('2', '1', '1', '', '181', '1', '1', '1', '1', '0', '2020-05-04 15:18:13', '2020-05-05 14:24:35');
INSERT INTO `ap_hot_articles` VALUES ('3', '0', '1', 'java', '0', '1246472013669851138', null, null, null, '0', '2020-05-04 00:02:47', '2020-05-05 14:24:35');
INSERT INTO `ap_hot_articles` VALUES ('4', '1', '1', 'java', '0', '1246472013669851138', null, null, null, '0', '2020-05-04 00:02:47', '2020-05-05 14:24:35');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='搜索热词表';

-- ----------------------------
-- Records of ap_hot_words
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
