-- MySQL dump 10.11
--
-- Host: 10.3.18.184    Database: test
-- ------------------------------------------------------
-- Server version	5.0.95

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `xweb_menu`
--

DROP TABLE IF EXISTS `xweb_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xweb_menu` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'id',
  `parent_id` bigint(20) NOT NULL COMMENT '父类ID',
  `parent_ids` varchar(255) default NULL COMMENT '所有父类编号',
  `name` varchar(100) default NULL COMMENT '菜单名称',
  `href` varchar(255) default NULL COMMENT 'href链接名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `is_show` char(1) NOT NULL COMMENT '是否展示',
  `permission` varchar(200) default NULL COMMENT '权限',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `UQ_xweb_menu_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xweb_menu`
--

LOCK TABLES `xweb_menu` WRITE;
/*!40000 ALTER TABLE `xweb_menu` DISABLE KEYS */;
INSERT INTO `xweb_menu` VALUES (1,0,'0,','顶级菜单',NULL,0,'1',NULL),(2,1,'0,1,','测试菜单','',0,'1',''),(3,2,'0,1,2,','测试','/test',0,'1',''),(4,1,'0,1,','hello','',0,'1',''),(6,4,'0,1,4,','word','/account/menu',0,'1',''),(7,1,'0,1,','campus','',0,'1',''),(8,7,'0,1,7,','user','/account/user',0,'1','');
/*!40000 ALTER TABLE `xweb_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xweb_role`
--

DROP TABLE IF EXISTS `xweb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xweb_role` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'id',
  `name` varchar(255) NOT NULL COMMENT '角色名称',
  `detail` varchar(255) NOT NULL COMMENT '明细',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `UQ_xweb_role_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xweb_role`
--

LOCK TABLES `xweb_role` WRITE;
/*!40000 ALTER TABLE `xweb_role` DISABLE KEYS */;
INSERT INTO `xweb_role` VALUES (1,'admin','超级管理员'),(2,'user','普通用户');
/*!40000 ALTER TABLE `xweb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xweb_role_menu`
--

DROP TABLE IF EXISTS `xweb_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xweb_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY  (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xweb_role_menu`
--

LOCK TABLES `xweb_role_menu` WRITE;
/*!40000 ALTER TABLE `xweb_role_menu` DISABLE KEYS */;
INSERT INTO `xweb_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,6),(1,7),(1,8),(2,1),(2,2),(2,3),(2,4),(2,6);
/*!40000 ALTER TABLE `xweb_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xweb_test`
--

DROP TABLE IF EXISTS `xweb_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xweb_test` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `msg` varchar(200) NOT NULL,
  `detail` varchar(200) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='测试表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xweb_test`
--

LOCK TABLES `xweb_test` WRITE;
/*!40000 ALTER TABLE `xweb_test` DISABLE KEYS */;
INSERT INTO `xweb_test` VALUES (1,'test',NULL);
/*!40000 ALTER TABLE `xweb_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xweb_user`
--

DROP TABLE IF EXISTS `xweb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xweb_user` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'id',
  `login_name` varchar(255) NOT NULL COMMENT '登录名',
  `name` varchar(50) default NULL COMMENT '用户名',
  `password` varchar(255) default NULL COMMENT '密码',
  `salt` varchar(64) default NULL COMMENT '加密密文',
  `email` varchar(128) default NULL COMMENT '邮箱',
  `status` varchar(32) default NULL COMMENT '状态',
  `staff_no` varchar(50) default NULL COMMENT '工号',
  `phone` varchar(128) default NULL COMMENT '电话',
  `mobile` varchar(128) default NULL COMMENT '手机号',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `UQ_xweb_user_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xweb_user`
--

LOCK TABLES `xweb_user` WRITE;
/*!40000 ALTER TABLE `xweb_user` DISABLE KEYS */;
INSERT INTO `xweb_user` VALUES (1,'admin','Admin','691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','yong.cao@renren-inc.com','1','CIAC010988','',''),(2,'user','User','21955a5c3d51126b77e6fb219dd89ed03c000962','68562c9f3268d72d','test@renren-inc.com','1','','','');
/*!40000 ALTER TABLE `xweb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xweb_user_role`
--

DROP TABLE IF EXISTS `xweb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xweb_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY  (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xweb_user_role`
--

LOCK TABLES `xweb_user_role` WRITE;
/*!40000 ALTER TABLE `xweb_user_role` DISABLE KEYS */;
INSERT INTO `xweb_user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `xweb_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-19 13:44:53
