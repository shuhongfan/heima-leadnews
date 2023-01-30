/*
SQLyog Ultimate v8.32 
MySQL - 5.7.25 : Database - leadnews_user
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`leadnews_user` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `leadnews_user`;

/*Table structure for table `ap_user` */

DROP TABLE IF EXISTS `ap_user`;

CREATE TABLE `ap_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `salt` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码、通信等加密盐',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码,md5加密',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `sex` tinyint(1) unsigned DEFAULT NULL COMMENT '0 男\r\n            1 女\r\n            2 未知',
  `is_certification` tinyint(1) unsigned DEFAULT NULL COMMENT '0 未\r\n            1 是',
  `is_identity_authentication` tinyint(1) DEFAULT NULL COMMENT '是否身份认证',
  `status` tinyint(1) unsigned DEFAULT NULL COMMENT '0正常\r\n            1锁定',
  `flag` tinyint(1) unsigned DEFAULT NULL COMMENT '0 普通用户\r\n            1 自媒体人\r\n            2 大V',
  `created_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户信息表';

/*Data for the table `ap_user` */

insert  into `ap_user`(`id`,`salt`,`name`,`password`,`phone`,`image`,`sex`,`is_certification`,`is_identity_authentication`,`status`,`flag`,`created_time`) values (1,'abc','zhangsan','abc','13511223453',NULL,1,NULL,NULL,1,1,'2020-03-19 23:22:07'),(2,'abc','lisi','abc','13511223454','',1,NULL,NULL,1,1,'2020-03-19 23:22:07'),(3,'sdsa','wangwu','wangwu','13511223455',NULL,NULL,NULL,NULL,NULL,1,NULL),(4,'123abc','admin','81e158e10201b6d7aee6e35eaf744796','13511223456',NULL,1,NULL,NULL,1,1,'2020-03-30 16:36:32'),(5,'123abc','suwukong','81e158e10201b6d7aee6e35eaf744796','13511223458',NULL,1,NULL,NULL,1,1,'2020-08-01 11:09:57'),(6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `ap_user_article_list` */

DROP TABLE IF EXISTS `ap_user_article_list`;

CREATE TABLE `ap_user_article_list` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `channel_id` int(11) unsigned DEFAULT NULL COMMENT '频道ID',
  `article_id` int(11) unsigned DEFAULT NULL COMMENT '动态ID',
  `is_show` tinyint(1) unsigned DEFAULT NULL COMMENT '是否展示',
  `recommend_time` datetime DEFAULT NULL COMMENT '推荐时间',
  `is_read` tinyint(1) unsigned DEFAULT NULL COMMENT '是否阅读',
  `strategy_id` int(5) unsigned DEFAULT NULL COMMENT '推荐算法',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户文章列表';

/*Data for the table `ap_user_article_list` */

/*Table structure for table `ap_user_channel` */

DROP TABLE IF EXISTS `ap_user_channel`;

CREATE TABLE `ap_user_channel` (
  `id` int(11) unsigned NOT NULL,
  `channel_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '文章ID',
  `name` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ord` tinyint(2) DEFAULT NULL COMMENT '频道排序',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户频道信息表';

/*Data for the table `ap_user_channel` */

/*Table structure for table `ap_user_dynamic_list` */

DROP TABLE IF EXISTS `ap_user_dynamic_list`;

CREATE TABLE `ap_user_dynamic_list` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `dynamic_id` int(11) unsigned DEFAULT NULL COMMENT '动态ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户动态列表';

/*Data for the table `ap_user_dynamic_list` */

/*Table structure for table `ap_user_equipment` */

DROP TABLE IF EXISTS `ap_user_equipment`;

CREATE TABLE `ap_user_equipment` (
  `id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `equipment_id` int(11) unsigned DEFAULT NULL COMMENT '设备ID',
  `last_time` datetime DEFAULT NULL COMMENT '上次使用时间',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户设备信息表';

/*Data for the table `ap_user_equipment` */

/*Table structure for table `ap_user_fan` */

DROP TABLE IF EXISTS `ap_user_fan`;

CREATE TABLE `ap_user_fan` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `fans_id` int(11) unsigned DEFAULT NULL COMMENT '粉丝ID',
  `fans_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '粉丝昵称',
  `level` tinyint(1) unsigned DEFAULT NULL COMMENT '粉丝忠实度\r\n            0 正常\r\n            1 潜力股\r\n            2 勇士\r\n            3 铁杆\r\n            4 老铁',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_display` tinyint(1) unsigned DEFAULT NULL COMMENT '是否可见我动态',
  `is_shield_letter` tinyint(1) unsigned DEFAULT NULL COMMENT '是否屏蔽私信',
  `is_shield_comment` tinyint(1) unsigned DEFAULT NULL COMMENT '是否屏蔽评论',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户粉丝信息表';

/*Data for the table `ap_user_fan` */

/*Table structure for table `ap_user_feedback` */

DROP TABLE IF EXISTS `ap_user_feedback`;

CREATE TABLE `ap_user_feedback` (
  `id` int(11) unsigned DEFAULT NULL COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人昵称',
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '私信内容',
  `images` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '反馈图片,多张逗号分隔',
  `is_solve` tinyint(1) unsigned DEFAULT NULL COMMENT '是否阅读',
  `solve_note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `solved_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户反馈信息表';

/*Data for the table `ap_user_feedback` */

/*Table structure for table `ap_user_follow` */

DROP TABLE IF EXISTS `ap_user_follow`;

CREATE TABLE `ap_user_follow` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `follow_id` int(11) unsigned DEFAULT NULL COMMENT '关注作者ID',
  `follow_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '粉丝昵称',
  `level` tinyint(1) unsigned DEFAULT NULL COMMENT '关注度\r\n            0 偶尔感兴趣\r\n            1 一般\r\n            2 经常\r\n            3 高度',
  `is_notice` tinyint(1) unsigned DEFAULT NULL COMMENT '是否动态通知',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户关注信息表';

/*Data for the table `ap_user_follow` */

/*Table structure for table `ap_user_identity` */

DROP TABLE IF EXISTS `ap_user_identity`;

CREATE TABLE `ap_user_identity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '账号ID',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `idno` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源名称',
  `font_image` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '正面照片',
  `back_image` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '背面照片',
  `hold_image` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手持照片',
  `industry` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '行业',
  `status` tinyint(1) unsigned DEFAULT NULL COMMENT '状态\r\n            0 创建中\r\n            1 待审核\r\n            2 审核失败\r\n            9 审核通过',
  `reason` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拒绝原因',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `submited_time` datetime DEFAULT NULL COMMENT '提交时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP身份认证信息表';

/*Data for the table `ap_user_identity` */

/*Table structure for table `ap_user_info` */

DROP TABLE IF EXISTS `ap_user_info`;

CREATE TABLE `ap_user_info` (
  `id` int(11) unsigned NOT NULL COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真是姓名',
  `idno` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号,aes加密',
  `company` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公司',
  `occupation` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职业',
  `age` tinyint(3) unsigned DEFAULT NULL COMMENT '年龄',
  `birthday` datetime DEFAULT NULL,
  `introduction` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '个人格言',
  `location` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '归属地',
  `fans` int(10) unsigned DEFAULT NULL COMMENT '粉丝数量',
  `follows` int(10) unsigned DEFAULT NULL COMMENT '关注数量',
  `is_recommend_me` tinyint(1) unsigned DEFAULT NULL COMMENT '是否允许推荐我给好友',
  `is_recommend_friend` tinyint(1) unsigned DEFAULT NULL COMMENT '是否允许给我推荐好友',
  `is_display_image` tinyint(1) unsigned DEFAULT NULL COMMENT '是否分享页面显示头像',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户信息表';

/*Data for the table `ap_user_info` */

/*Table structure for table `ap_user_letter` */

DROP TABLE IF EXISTS `ap_user_letter`;

CREATE TABLE `ap_user_letter` (
  `id` int(11) unsigned DEFAULT NULL COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `sender_id` int(11) unsigned DEFAULT NULL COMMENT '发送人ID',
  `sender_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人昵称',
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '私信内容',
  `is_read` tinyint(1) unsigned DEFAULT NULL COMMENT '是否阅读',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户私信信息表';

/*Data for the table `ap_user_letter` */

/*Table structure for table `ap_user_message` */

DROP TABLE IF EXISTS `ap_user_message`;

CREATE TABLE `ap_user_message` (
  `id` int(11) unsigned DEFAULT NULL COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `sender_id` int(11) unsigned DEFAULT NULL COMMENT '发送人ID',
  `sender_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人昵称',
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '私信内容',
  `type` tinyint(3) unsigned DEFAULT NULL COMMENT '消息类型\r\n            0 关注\r\n            1 取消关注\r\n            2 点赞文章\r\n            3 取消点赞文章\r\n            4 转发文章\r\n            5 收藏文章\r\n            6 点赞评论\r\n            7 审核通过评论\r\n            8 私信通知\r\n            9 评论通知\r\n            10 分享通知\r\n            \r\n            100 身份证审核通过\r\n            101 身份证审核拒绝\r\n            102 实名认证通过\r\n            103 实名认证失败\r\n            104 自媒体人祝贺\r\n            105 异常登录通知\r\n            106 反馈回复\r\n            107 转发通知',
  `is_read` tinyint(1) unsigned DEFAULT NULL COMMENT '是否阅读',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户消息通知信息表';

/*Data for the table `ap_user_message` */

/*Table structure for table `ap_user_realname` */

DROP TABLE IF EXISTS `ap_user_realname`;

CREATE TABLE `ap_user_realname` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '账号ID',
  `name` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户名称',
  `idno` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源名称',
  `font_image` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '正面照片',
  `back_image` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '背面照片',
  `hold_image` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手持照片',
  `live_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活体照片',
  `status` tinyint(1) unsigned DEFAULT NULL COMMENT '状态\r\n            0 创建中\r\n            1 待审核\r\n            2 审核失败\r\n            9 审核通过',
  `reason` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拒绝原因',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `submited_time` datetime DEFAULT NULL COMMENT '提交时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP实名认证信息表';

/*Data for the table `ap_user_realname` */

insert  into `ap_user_realname`(`id`,`user_id`,`name`,`idno`,`font_image`,`back_image`,`hold_image`,`live_image`,`status`,`reason`,`created_time`,`submited_time`,`updated_time`) values (1,1,'zhangsan','512335455602781278','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbHSAQlqFAAXIZNzAq9E126.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbF6AR16RAAZB2e1EsOg460.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbDeAH2qoAAbD_WiUJfk745.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9ba9qANVEdAAS25KJlEVE291.jpg',9,'','2019-07-30 14:34:28','2019-07-30 14:34:30','2019-07-12 06:48:04'),(2,2,'lisi','512335455602781279','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbHSAQlqFAAXIZNzAq9E126.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbF6AR16RAAZB2e1EsOg460.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbDeAH2qoAAbD_WiUJfk745.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9ba9qANVEdAAS25KJlEVE291.jpg',1,'','2019-07-11 17:21:18','2019-07-11 17:21:20','2019-07-12 06:48:04'),(3,3,'wangwu6666','512335455602781276','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbHSAQlqFAAXIZNzAq9E126.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbF6AR16RAAZB2e1EsOg460.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbDeAH2qoAAbD_WiUJfk745.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9ba9qANVEdAAS25KJlEVE291.jpg',9,'','2019-07-11 17:21:18','2019-07-11 17:21:20','2019-07-12 06:48:04'),(5,5,'suwukong','512335455602781279','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbHSAQlqFAAXIZNzAq9E126.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbF6AR16RAAZB2e1EsOg460.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9bbDeAH2qoAAbD_WiUJfk745.jpg','http://161.189.111.227/group1/M00/00/00/rBFwgF9ba9qANVEdAAS25KJlEVE291.jpg',9,'','2020-08-01 11:10:31','2020-08-01 11:10:34','2021-11-18 21:59:23');

/*Table structure for table `ap_user_search` */

DROP TABLE IF EXISTS `ap_user_search`;

CREATE TABLE `ap_user_search` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '用户ID',
  `keyword` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '搜索词',
  `status` tinyint(2) unsigned DEFAULT NULL COMMENT '当前状态0 无效 1有效',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP用户搜索信息表';

/*Data for the table `ap_user_search` */

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `undo_log` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
