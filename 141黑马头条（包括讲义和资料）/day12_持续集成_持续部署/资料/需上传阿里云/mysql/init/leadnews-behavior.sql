/*
SQLyog Ultimate v8.32 
MySQL - 5.7.25 : Database - leadnews_behavior
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`leadnews_behavior` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `leadnews_behavior`;

/*Table structure for table `ap_behavior_entry` */

DROP TABLE IF EXISTS `ap_behavior_entry`;

CREATE TABLE `ap_behavior_entry` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '实体类型\r\n            0终端设备\r\n            1用户',
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP行为实体表,一个行为实体可能是用户或者设备，或者其它';

/*Data for the table `ap_behavior_entry` */

insert  into `ap_behavior_entry`(`id`,`type`,`entry_id`,`created_time`) values (1,1,4,'2020-04-07 22:17:22'),(2,0,88888888,'2020-09-29 17:09:00');

/*Table structure for table `ap_follow_behavior` */

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

/*Data for the table `ap_follow_behavior` */

insert  into `ap_follow_behavior`(`id`,`entry_id`,`article_id`,`follow_id`,`created_time`) values (1306493322981928961,1,1303156149041758210,4,'2020-09-17 15:20:56'),(1306493386580160514,1,1303156149041758210,4,'2020-09-17 15:21:11');

/*Table structure for table `ap_forward_behavior` */

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

/*Data for the table `ap_forward_behavior` */

/*Table structure for table `ap_likes_behavior` */

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

/*Data for the table `ap_likes_behavior` */

insert  into `ap_likes_behavior`(`id`,`entry_id`,`article_id`,`type`,`operation`,`created_time`) values (1306556220991197185,1,1303156149041758210,0,0,'2020-09-17 19:30:50'),(1306826584845467649,1,1302865474094120961,0,0,'2020-09-18 13:25:12');

/*Table structure for table `ap_read_behavior` */

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

/*Data for the table `ap_read_behavior` */

insert  into `ap_read_behavior`(`id`,`entry_id`,`article_id`,`count`,`read_duration`,`percentage`,`load_duration`,`created_time`,`updated_time`) values (1306791787066572802,1,1302977178887004162,2,9200,99,0,'2020-09-18 11:06:55','2020-09-18 11:07:37'),(1306820004724883458,1,1303156149041758210,40,5600,99,0,'2020-09-18 12:59:03','2020-09-27 19:09:56'),(1306826540398428161,1,1302977754114826243,1,1600,0,1600,'2020-09-18 13:25:02',NULL),(1306826607201107970,1,1302865474094120961,1,13000,97,0,'2020-09-18 13:25:17',NULL),(1309389244548116482,1,1302977754114826241,1,2500,99,0,'2020-09-25 15:08:17',NULL),(1314024363527266305,1,1302865008438296600,1,1900,0,1900,'2020-10-08 10:06:36',NULL),(1314050329599303681,1,1302865306489733122,1,1700,0,100,'2020-10-08 11:49:47',NULL),(1314050541399072769,1,1302862387124125698,1,44900,99,0,'2020-10-08 11:50:38',NULL);

/*Table structure for table `ap_share_behavior` */

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

/*Data for the table `ap_share_behavior` */

/*Table structure for table `ap_show_behavior` */

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

/*Data for the table `ap_show_behavior` */

/*Table structure for table `ap_unlikes_behavior` */

DROP TABLE IF EXISTS `ap_unlikes_behavior`;

CREATE TABLE `ap_unlikes_behavior` (
  `id` bigint(20) unsigned NOT NULL,
  `entry_id` int(11) unsigned DEFAULT NULL COMMENT '实体ID',
  `article_id` bigint(20) unsigned DEFAULT NULL COMMENT '文章ID',
  `type` tinyint(2) unsigned DEFAULT NULL COMMENT '0 不喜欢\r\n            1 取消不喜欢',
  `created_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='APP不喜欢行为表';

/*Data for the table `ap_unlikes_behavior` */

insert  into `ap_unlikes_behavior`(`id`,`entry_id`,`article_id`,`type`,`created_time`) values (11222,1,1303156149041758210,1,'2020-04-07 23:27:23');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
