/*
SQLyog Ultimate v8.32 
MySQL - 5.7.25 : Database - leadnews_admin
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`leadnews_admin` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `leadnews_admin`;

/*Table structure for table `ad_article_statistics` */

DROP TABLE IF EXISTS `ad_article_statistics`;

CREATE TABLE `ad_article_statistics` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `article_we_media` int(11) unsigned DEFAULT NULL COMMENT '主账号ID',
  `article_crawlers` int(11) unsigned DEFAULT NULL COMMENT '子账号ID',
  `channel_id` int(11) unsigned DEFAULT NULL COMMENT '频道ID',
  `read_20` int(11) unsigned DEFAULT NULL COMMENT '草读量',
  `read_100` int(11) unsigned DEFAULT NULL COMMENT '读完量',
  `read_count` int(11) unsigned DEFAULT NULL COMMENT '阅读量',
  `comment` int(11) unsigned DEFAULT NULL COMMENT '评论量',
  `follow` int(11) unsigned DEFAULT NULL COMMENT '关注量',
  `collection` int(11) unsigned DEFAULT NULL COMMENT '收藏量',
  `forward` int(11) unsigned DEFAULT NULL COMMENT '转发量',
  `likes` int(11) unsigned DEFAULT NULL COMMENT '点赞量',
  `unlikes` int(11) unsigned DEFAULT NULL COMMENT '不喜欢',
  `unfollow` int(11) unsigned DEFAULT NULL COMMENT 'unfollow',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='文章数据统计表';

/*Data for the table `ad_article_statistics` */

/*Table structure for table `ad_channel` */

DROP TABLE IF EXISTS `ad_channel`;

CREATE TABLE `ad_channel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道描述',
  `is_default` tinyint(1) unsigned DEFAULT NULL COMMENT '是否默认频道',
  `status` tinyint(1) unsigned DEFAULT NULL,
  `ord` tinyint(3) unsigned DEFAULT NULL COMMENT '默认排序',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='频道信息表';

/*Data for the table `ad_channel` */

insert  into `ad_channel`(`id`,`name`,`description`,`is_default`,`status`,`ord`,`created_time`) values (4,'Python3333','未来的语言',1,0,4,'2019-08-16 10:55:41'),(5,'Weex','省钱神器',1,1,5,'2019-08-16 10:55:45'),(6,'大数据','不会，则不要说自己是搞互联网的',1,1,6,'2019-08-05 17:33:17'),(7,'Docker','向未来致敬',1,1,7,'2019-08-16 10:56:26'),(46,'nodejs','大前端',NULL,1,10,'2020-09-11 15:06:11'),(47,'nuxt.js','服务端渲染框架',NULL,1,12,'2020-09-11 15:09:30'),(48,'hello','',1,1,0,'2021-11-27 11:03:52');

/*Table structure for table `ad_channel_label` */

DROP TABLE IF EXISTS `ad_channel_label`;

CREATE TABLE `ad_channel_label` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_id` int(11) unsigned DEFAULT NULL,
  `label_id` int(11) unsigned DEFAULT NULL COMMENT '标签ID',
  `ord` int(5) unsigned DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1118 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='频道标签信息表';

/*Data for the table `ad_channel_label` */

insert  into `ad_channel_label`(`id`,`channel_id`,`label_id`,`ord`) values (1108,1,1,0),(1109,1,8,0),(1110,7,7,0),(1111,7,9,0),(1112,3,3,0),(1113,7,2,0),(1114,3,4,0),(1115,5,5,0),(1116,4,6,0),(1117,6,10,0);

/*Table structure for table `ad_function` */

DROP TABLE IF EXISTS `ad_function`;

CREATE TABLE `ad_function` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '功能名称',
  `code` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '功能代码',
  `parent_id` int(11) unsigned DEFAULT NULL COMMENT '父功能',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='页面功能信息表';

/*Data for the table `ad_function` */

/*Table structure for table `ad_label` */

DROP TABLE IF EXISTS `ad_label`;

CREATE TABLE `ad_label` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道名称',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '0系统增加\r\n            1人工增加',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=24237 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='标签信息表';

/*Data for the table `ad_label` */

insert  into `ad_label`(`id`,`name`,`type`,`created_time`) values (1,'java',0,'2019-08-22 16:30:16'),(2,'docker',0,'2019-08-22 16:30:52'),(3,'mysql',0,'2019-08-22 16:31:17'),(4,'vue',0,'2019-08-22 16:35:39'),(5,'Weex',0,'2019-08-22 16:36:08'),(6,'Python',0,'2019-08-22 16:36:34'),(7,'大数据',0,'2019-08-22 16:36:48'),(8,'spring',0,'2019-08-22 16:39:11'),(9,'hbase',0,'2019-08-22 16:39:35'),(10,'hive',0,'2019-08-22 16:43:44'),(23858,'工具',1,'2019-08-22 16:43:55'),(23859,'c',1,'2019-08-22 16:44:01'),(23860,'电话',1,'2019-08-22 16:44:22'),(23861,'照片',1,'2019-08-22 16:44:22'),(23862,'咨询',1,'2019-08-22 16:44:22'),(23863,'智能',1,'2019-08-22 16:46:00'),(23864,'人工智能',1,'2019-08-22 16:46:00'),(23865,'AI',1,'2019-08-22 16:46:00'),(23866,'智能协会',1,'2019-08-22 16:46:00'),(23867,'人工智能协会',1,'2019-08-22 16:46:00'),(23868,'5G',1,'2019-08-22 16:47:21'),(23869,'高通',1,'2019-08-22 16:47:21'),(23870,'苹果',1,'2019-08-22 16:47:21'),(23871,'英特尔',1,'2019-08-22 16:47:21'),(23872,'智能手机',1,'2019-08-22 16:47:21'),(23873,'google',1,'2019-08-22 16:47:49'),(23874,'芯片',1,'2019-08-22 16:47:49'),(23875,'交互',1,'2019-08-22 16:47:49'),(23876,'智能交互',1,'2019-08-22 16:47:49'),(23877,'手势交互',1,'2019-08-22 16:47:49'),(23878,'智慧酒店',1,'2019-08-22 16:48:54'),(23879,'人脸识别',1,'2019-08-22 16:49:06'),(23880,'软硬件开发流程',1,'2019-08-22 16:49:26'),(23881,'软硬件结合',1,'2019-08-22 16:50:23'),(23882,'React',1,'2019-08-22 17:18:59'),(23883,'前端技术',1,'2019-08-22 17:18:59'),(23903,'IDEA',1,'2019-08-22 17:22:21'),(23904,'git',1,'2019-08-22 17:32:54'),(23905,'java学习',1,'2019-08-22 17:43:34'),(23906,'机器学习',1,'2019-08-22 17:46:40'),(23907,'陆奇',1,'2019-08-22 17:46:55'),(23908,'设计模式',1,'2019-08-22 17:47:40'),(23909,'谷歌',1,'2019-08-22 17:48:11'),(23916,'工业数字化',1,'2019-08-22 17:49:16'),(23949,'个人生活',1,'2019-08-22 17:57:38'),(23950,'安徽',1,'2019-08-22 17:57:38'),(23951,'黄山',1,'2019-08-22 17:57:38'),(23952,'SVN信息',1,'2019-08-22 18:00:27'),(23953,'清除',1,'2019-08-22 18:00:27'),(23954,'删除',1,'2019-08-22 18:00:27'),(23955,'svn',1,'2019-08-22 18:00:27'),(23956,'排序',1,'2019-08-22 18:01:27'),(23957,'快排',1,'2019-08-22 18:01:27'),(23958,'归并排序',1,'2019-08-22 18:01:27'),(23959,'程序人生',1,'2019-08-22 18:05:25'),(23960,'毕业一年',1,'2019-08-22 18:05:25'),(23961,'毕业总结',1,'2019-08-22 18:05:25'),(23962,'毕业感想',1,'2019-08-22 18:05:25'),(23979,'dom',1,'2019-08-22 18:09:39'),(23980,'javascript',1,'2019-08-22 18:09:39'),(23981,'web开发',1,'2019-08-22 18:09:39'),(23995,'IT',1,'2019-08-22 18:13:50'),(23996,'科技',1,'2019-08-22 18:13:50'),(23998,'自动化',1,'2019-08-22 18:16:20'),(23999,'运维',1,'2019-08-22 18:16:20'),(24000,'ansible',1,'2019-08-22 18:16:20'),(24001,'入门',1,'2019-08-22 18:16:20'),(24002,'c#',1,'2019-08-22 18:16:45'),(24003,'正则',1,'2019-08-22 18:16:45'),(24004,'regex',1,'2019-08-22 18:16:45'),(24005,'字符串',1,'2019-08-22 18:16:45'),(24015,'openstack',1,'2019-08-22 18:19:42'),(24016,'k8s',1,'2019-08-22 18:20:47'),(24017,'kubernetes',1,'2019-08-22 18:20:47'),(24018,'kubectl',1,'2019-08-22 18:20:47'),(24019,'supervisor',1,'2019-08-22 18:24:43'),(24020,'linux',1,'2019-08-22 18:24:43'),(24021,'进程管理',1,'2019-08-22 18:24:43'),(24109,'百度网盘',1,'2019-08-22 19:14:19'),(24110,'百度云',1,'2019-08-22 19:14:19'),(24111,'免费',1,'2019-08-22 19:14:19'),(24112,'不限速',1,'2019-08-22 19:14:19'),(24113,'破解',1,'2019-08-22 19:14:19'),(24123,'脚本',1,'2019-08-22 19:14:26'),(24124,'作业',1,'2019-08-22 19:14:26'),(24125,'自动化运维',1,'2019-08-22 19:14:26'),(24126,'极客头条',1,'2019-08-22 20:34:33'),(24198,'业界新闻',1,'2019-08-23 07:28:27'),(24206,'杂谈',1,'2019-08-23 07:31:09'),(24207,'零信任网络',1,'2019-08-23 07:33:37'),(24208,'网络安全',1,'2019-08-23 07:33:37'),(24209,'Go语言',1,'2019-08-23 07:33:54'),(24210,'程序员',1,'2019-08-23 07:33:54'),(24223,'Android',1,'2019-08-23 07:35:42'),(24224,'架构',1,'2019-08-23 07:35:42'),(24232,'flume',1,'2019-08-23 07:37:05'),(24233,'hive优化',1,'2019-08-23 07:37:27'),(24234,'parquet',1,'2019-08-23 07:37:27'),(24235,'orc',1,'2019-08-23 07:37:27'),(24236,'snappy',1,'2019-08-23 07:37:27');

/*Table structure for table `ad_menu` */

DROP TABLE IF EXISTS `ad_menu`;

CREATE TABLE `ad_menu` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单名称',
  `code` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单代码',
  `parent_id` int(11) unsigned DEFAULT NULL COMMENT '父菜单',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='菜单资源信息表';

/*Data for the table `ad_menu` */

/*Table structure for table `ad_recommend_strategy` */

DROP TABLE IF EXISTS `ad_recommend_strategy`;

CREATE TABLE `ad_recommend_strategy` (
  `id` int(5) unsigned NOT NULL COMMENT '主键',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '策略名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '策略描述',
  `is_enable` tinyint(1) DEFAULT NULL COMMENT '是否有效',
  `group_id` int(5) unsigned DEFAULT NULL COMMENT '分组ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='推荐策略信息表';

/*Data for the table `ad_recommend_strategy` */

/*Table structure for table `ad_role` */

DROP TABLE IF EXISTS `ad_role`;

CREATE TABLE `ad_role` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `description` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色描述',
  `is_enable` tinyint(1) unsigned DEFAULT NULL COMMENT '是否有效',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色信息表';

/*Data for the table `ad_role` */

/*Table structure for table `ad_role_auth` */

DROP TABLE IF EXISTS `ad_role_auth`;

CREATE TABLE `ad_role_auth` (
  `id` int(11) unsigned NOT NULL,
  `role_id` int(11) unsigned DEFAULT NULL COMMENT '角色ID',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '资源类型\r\n            0 菜单\r\n            1 功能',
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '资源ID',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色权限信息表';

/*Data for the table `ad_role_auth` */

/*Table structure for table `ad_sensitive` */

DROP TABLE IF EXISTS `ad_sensitive`;

CREATE TABLE `ad_sensitive` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sensitives` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '敏感词',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='敏感词信息表';

/*Data for the table `ad_sensitive` */

insert  into `ad_sensitive`(`id`,`sensitives`,`created_time`) values (3104,'冰毒','2020-03-03 15:38:51'),(3105,'法轮功','2020-03-03 15:38:51'),(3109,'111','2020-03-16 21:49:17'),(3110,'222','2020-03-16 21:49:21'),(3111,'33','2020-03-16 21:49:23'),(3112,'44','2020-03-16 21:49:25'),(3113,'55','2020-03-16 21:49:28'),(3114,'66','2020-03-16 21:49:30');

/*Table structure for table `ad_strategy_group` */

DROP TABLE IF EXISTS `ad_strategy_group`;

CREATE TABLE `ad_strategy_group` (
  `id` int(5) unsigned NOT NULL COMMENT '主键',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '策略名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '策略描述',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='推荐策略分组信息表';

/*Data for the table `ad_strategy_group` */

/*Table structure for table `ad_user` */

DROP TABLE IF EXISTS `ad_user`;

CREATE TABLE `ad_user` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录用户名',
  `password` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盐',
  `nickname` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `phone` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint(11) unsigned DEFAULT NULL COMMENT '状态\r\n            0 暂时不可用\r\n            1 永久不可用\r\n            9 正常可用',
  `email` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员用户信息表';

/*Data for the table `ad_user` */

insert  into `ad_user`(`id`,`name`,`password`,`salt`,`nickname`,`image`,`phone`,`status`,`email`,`login_time`,`created_time`) values (1,'wukong','123',NULL,'mo',NULL,'13320325525',9,'wukong@qq.com','2019-07-30 10:16:41','2019-07-30 10:16:43'),(2,'admin','5d4e1a406d4a9edbf7b4f10c2a390405','123abc','ad',NULL,'13320325528',9,'admin@qq.com','2020-03-04 17:07:37','2020-03-04 17:07:40'),(3,'guest','34e20b52f5bd120db806e57e27f47ed0','123456','gu',NULL,'13412345676',9,'guest@qq.com','2021-11-24 11:31:52','2020-07-30 15:00:06');

/*Table structure for table `ad_user_equipment` */

DROP TABLE IF EXISTS `ad_user_equipment`;

CREATE TABLE `ad_user_equipment` (
  `id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '0PC\r\n            1ANDROID\r\n            2IOS\r\n            3PAD\r\n            9 其他',
  `version` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备版本',
  `sys` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备系统',
  `no` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备唯一号，MD5加密',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员设备信息表';

/*Data for the table `ad_user_equipment` */

/*Table structure for table `ad_user_login` */

DROP TABLE IF EXISTS `ad_user_login`;

CREATE TABLE `ad_user_login` (
  `id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `equipment_id` int(11) unsigned DEFAULT NULL COMMENT '登录设备ID',
  `ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录IP',
  `address` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录地址',
  `longitude` decimal(5,5) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(5,5) DEFAULT NULL COMMENT '纬度',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员登录行为信息表';

/*Data for the table `ad_user_login` */

/*Table structure for table `ad_user_opertion` */

DROP TABLE IF EXISTS `ad_user_opertion`;

CREATE TABLE `ad_user_opertion` (
  `id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `equipment_id` int(11) unsigned DEFAULT NULL COMMENT '登录设备ID',
  `ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录IP',
  `address` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录地址',
  `type` int(5) DEFAULT NULL COMMENT '操作类型',
  `description` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作描述',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员操作行为信息表';

/*Data for the table `ad_user_opertion` */

/*Table structure for table `ad_user_role` */

DROP TABLE IF EXISTS `ad_user_role`;

CREATE TABLE `ad_user_role` (
  `id` int(11) unsigned NOT NULL,
  `role_id` int(11) unsigned DEFAULT NULL COMMENT '角色ID',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员角色信息表';

/*Data for the table `ad_user_role` */

/*Table structure for table `ad_vistor_statistics` */

DROP TABLE IF EXISTS `ad_vistor_statistics`;

CREATE TABLE `ad_vistor_statistics` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `activity` int(11) unsigned DEFAULT NULL COMMENT '日活',
  `vistor` int(11) unsigned DEFAULT NULL COMMENT '访问量',
  `ip` int(11) unsigned DEFAULT NULL COMMENT 'IP量',
  `register` int(11) unsigned DEFAULT NULL COMMENT '注册量',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='访问数据统计表';

/*Data for the table `ad_vistor_statistics` */

/*Table structure for table `undo_log` */

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
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `undo_log` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
