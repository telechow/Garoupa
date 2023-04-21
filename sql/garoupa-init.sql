-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 192.168.0.200    Database: garoupa
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `garoupa`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `garoupa` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `garoupa`;

--
-- Table structure for table `cosid`
--

DROP TABLE IF EXISTS `cosid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cosid` (
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '{namespace}.{name}',
  `last_max_id` bigint NOT NULL DEFAULT '0',
  `last_fetch_time` bigint NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cosid`
--

LOCK TABLES `cosid` WRITE;
/*!40000 ALTER TABLE `cosid` DISABLE KEYS */;
INSERT INTO `cosid` VALUES ('Garoupa.AuditLog',70,1681959003),('Garoupa.LoginLog',162,1681958953),('Garoupa.Permission',2901,1681958954),('Garoupa.Resource',2860,1681958954),('Garoupa.Role',3220,1681958953),('Garoupa.RolePermissionRelation',2850,1681958954),('Garoupa.RoleResourceRelation',2850,1681958954),('Garoupa.SystemDict',522,1681958953),('Garoupa.SystemDictItem',525,1681958953),('Garoupa.SystemParam',1055,1681958953),('Garoupa.SystemParamCategory',1076,1681958953),('Garoupa.User',3850,1681958954),('Garoupa.UserRoleRelation',2850,1681958953),('Garoupa.__share__',323000,1681958953);
/*!40000 ALTER TABLE `cosid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_audit_log`
--

DROP TABLE IF EXISTS `garoupa_audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_audit_log` (
  `id` bigint NOT NULL COMMENT '审计日志id',
  `title` char(250) COLLATE utf8mb4_general_ci NOT NULL COMMENT '审计日志标题，非空；限250字',
  `user_id` bigint NOT NULL COMMENT '请求用户id，非空',
  `remote_ip` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求用户ip地址，非空',
  `user_agent` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户代理，非空',
  `request_uri` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求uri，非空',
  `method` char(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方法，非空；http的请求方法',
  `request_params` varchar(5000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数的json，非空',
  `execute_duration` bigint NOT NULL COMMENT '执行时长，非空；单位毫秒',
  `log_type` int NOT NULL COMMENT '日志类型，非空；0：正常；10：错误',
  `exception_message` varchar(5000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常信息，只有日志类型为错误才存在异常信息',
  `create_time` datetime NOT NULL COMMENT '请求时间，非空',
  PRIMARY KEY (`id`),
  KEY `garoupa_audit_log_title_user_id_create_time_index` (`title`,`user_id`,`create_time` DESC),
  FULLTEXT KEY `garoupa_audit_log_remote_ip_fulltext_index` (`remote_ip`) /*!50100 WITH PARSER `ngram` */ 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='审计日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_audit_log`
--

LOCK TABLES `garoupa_audit_log` WRITE;
/*!40000 ALTER TABLE `garoupa_audit_log` DISABLE KEYS */;
INSERT INTO `garoupa_audit_log` VALUES (11,'设置系统字典的锁定标识',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/system/dict/set/lock/16','PUT','{}',143,0,NULL,'2023-04-20 09:45:32'),(12,'设置系统字典的锁定标识',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/system/dict/set/lock/17','PUT','{}',13,0,NULL,'2023-04-20 09:46:29'),(13,'设置系统字典的锁定标识',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/system/dict/set/lock/98','PUT','{}',10,0,NULL,'2023-04-20 09:46:33'),(14,'设置系统字典的锁定标识',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/system/dict/set/lock/309','PUT','{}',10,0,NULL,'2023-04-20 09:46:38'),(15,'设置系统字典的锁定标识',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/system/dict/set/lock/340','PUT','{}',11,0,NULL,'2023-04-20 09:46:42'),(16,'设置系统字典的锁定标识',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/system/dict/set/lock/431','PUT','{}',11,0,NULL,'2023-04-20 09:46:46'),(17,'设置系统字典的锁定标识',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/system/dict/set/lock/462','PUT','{}',13,0,NULL,'2023-04-20 09:46:51'),(60,'修改权限',1,'127.0.0.1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36','/m/permission/update/2861','PUT','{\"dto\":{\"permissionCode\":\"log:audit-log:page\",\"permissionName\":\"分页查询审计日志\"},\"id\":\"2861\"}',126,0,NULL,'2023-04-20 10:50:03');
/*!40000 ALTER TABLE `garoupa_audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_login_log`
--

DROP TABLE IF EXISTS `garoupa_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_login_log` (
  `id` bigint NOT NULL COMMENT '登录日志id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id，如果登录失败则为null',
  `login_mode` char(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录方式，非空',
  `is_success` tinyint(1) NOT NULL COMMENT '是否登录成功，非空；false：失败，true：成功',
  `remote_ip` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ip，非空',
  `user_agent` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户代理，非空',
  `login_param` varchar(5000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录参数，非空；将多个参数转换成json',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  PRIMARY KEY (`id`),
  KEY `garoupa_login_log_create_time_index` (`create_time` DESC),
  FULLTEXT KEY `garoupa_login_log_remote_ip_fulltext_index` (`remote_ip`) /*!50100 WITH PARSER `ngram` */ 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_login_log`
--

LOCK TABLES `garoupa_login_log` WRITE;
/*!40000 ALTER TABLE `garoupa_login_log` DISABLE KEYS */;
INSERT INTO `garoupa_login_log` VALUES (11,NULL,'username-password-captcha',0,'127.0.0.1','PostmanRuntime-ApipostRuntime/1.1.0','{\"password\":[\"Garoupa2023\"],\"username\":[\"Garoupa\"]}','2023-04-18 16:16:09'),(52,1,'username-password-captcha',1,'127.0.0.1','PostmanRuntime-ApipostRuntime/1.1.0','{\"password\":[\"Garoupa2023\"],\"username\":[\"Garoupa\"]}','2023-04-18 16:31:25');
/*!40000 ALTER TABLE `garoupa_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_permission`
--

DROP TABLE IF EXISTS `garoupa_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_permission` (
  `id` bigint NOT NULL COMMENT '权限id',
  `permission_code` char(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编码，非空；限200字；形如“模块名:实体名:功能名”；唯一',
  `permission_name` char(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称，非空；限200字',
  `resource_id` bigint NOT NULL COMMENT '菜单资源id，非空',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `garoupa_permission_permission_code_deleted_tag_uindex` (`permission_code`,`deleted_tag`),
  KEY `garoupa_permission_resource_id_create_time_index` (`resource_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_permission`
--

LOCK TABLES `garoupa_permission` WRITE;
/*!40000 ALTER TABLE `garoupa_permission` DISABLE KEYS */;
INSERT INTO `garoupa_permission` VALUES (1,'*:*:*','超级管理员权限',1,1,'2023-03-30 22:29:10',1,'2023-03-30 22:29:10',0),(2481,'rbac:resource:create','创建资源',2455,1,'2023-04-14 20:19:40',1,'2023-04-14 20:19:40',0),(2482,'rbac:resource:update','修改资源',2455,1,'2023-04-14 20:19:56',1,'2023-04-14 20:19:56',0),(2483,'rbac:resource:logic-delete','逻辑删除资源',2455,1,'2023-04-14 20:20:20',1,'2023-04-14 20:20:20',0),(2484,'rbac:resource:tree-list','查询资源树列表',2455,1,'2023-04-14 20:20:34',1,'2023-04-14 20:20:34',0),(2485,'rbac:resource:menu-tree-list','查询菜单资源树列表',2455,1,'2023-04-14 20:20:48',1,'2023-04-14 20:20:48',0),(2486,'system-param:category:create','创建系统参数分类',2487,1,'2023-04-14 20:22:40',1,'2023-04-14 20:22:40',0),(2487,'system-param:category:update','修改系统参数分类',2487,1,'2023-04-14 20:22:54',1,'2023-04-14 20:22:54',0),(2488,'system-param:category:logic-delete','删除系统参数分类',2487,1,'2023-04-14 20:23:09',1,'2023-04-14 20:23:09',0),(2489,'system-param:category:list','查询系统参数分类列表',2487,1,'2023-04-14 20:23:23',1,'2023-04-14 20:23:23',0),(2490,'system-param:category:set-lock','设置系统参数分类的锁定标识',2487,1,'2023-04-14 20:23:36',1,'2023-04-14 20:23:36',0),(2491,'system-param:param:create','创建系统参数',2487,1,'2023-04-14 20:23:54',1,'2023-04-14 20:23:54',0),(2492,'system-param:param:update','修改系统参数',2487,1,'2023-04-14 20:24:06',1,'2023-04-14 20:24:06',0),(2493,'system-param:param:logic-delete','逻辑删除系统参数',2487,1,'2023-04-14 20:24:18',1,'2023-04-14 20:24:18',0),(2494,'system-param:param:page-by-category','根据系统参数分类分页查询系统参数',2487,1,'2023-04-14 20:24:32',1,'2023-04-14 20:24:32',0),(2495,'system-param:param:set-lock','设置系统参数的锁定标识',2487,1,'2023-04-14 20:24:46',1,'2023-04-14 20:24:46',0),(2496,'system-dict:dict:create','创建系统字典',2488,1,'2023-04-14 20:26:00',1,'2023-04-14 20:26:00',0),(2497,'system-dict:dict:update','修改系统字典',2488,1,'2023-04-14 20:26:11',1,'2023-04-14 20:26:11',0),(2498,'system-dict:dict:logic-delete','删除系统字典',2488,1,'2023-04-14 20:26:24',1,'2023-04-14 20:26:24',0),(2499,'system-dict:dict:set-lock','设置系统字典的锁定标识',2488,1,'2023-04-14 20:26:38',1,'2023-04-14 20:26:38',0),(2500,'system-dict:dict:page','分页查询系统字典',2488,1,'2023-04-14 20:26:51',1,'2023-04-14 20:26:51',0),(2501,'system-dict:dict-item:create','创建系统字典项',2488,1,'2023-04-14 20:27:09',1,'2023-04-14 20:27:09',0),(2502,'system-dict:dict-item:update','修改系统字典项',2488,1,'2023-04-14 20:27:20',1,'2023-04-14 20:27:20',0),(2503,'system-dict:dict-item:logic-delete','逻辑删除系统字典项',2488,1,'2023-04-14 20:27:39',1,'2023-04-14 20:27:39',0),(2504,'system-dict:dict-item:set-lock','设置系统字典项的锁定标识',2488,1,'2023-04-14 20:27:54',1,'2023-04-14 20:27:54',0),(2505,'system-dict:dict-item:set-effective-status','设置系统字典项的有效状态',2488,1,'2023-04-14 20:28:09',1,'2023-04-14 20:28:09',0),(2506,'system-dict:dict-item:list-by-system-dict','根据系统字典id查询系统字典项列表',2488,1,'2023-04-14 20:28:23',1,'2023-04-14 20:28:23',0),(2507,'system-dict:dict-item:list-by-system-dict-code','根据系统字典编码查询系统字典项列表',2488,1,'2023-04-14 20:28:40',1,'2023-04-14 20:28:40',0),(2508,'rbac:permission:create','创建权限',2456,1,'2023-04-14 20:29:35',1,'2023-04-14 20:29:35',0),(2509,'rbac:permission:update','修改权限',2456,1,'2023-04-14 20:29:48',1,'2023-04-14 20:29:48',0),(2510,'rbac:permission:logic-delete','逻辑删除权限',2456,1,'2023-04-14 20:30:03',1,'2023-04-14 20:30:03',0),(2511,'rbac:permission:page-by-menu-id','分页查询指定菜单中的权限',2456,1,'2023-04-14 20:30:16',1,'2023-04-14 20:30:16',0),(2672,'rbac:role:create','创建角色',2454,1,'2023-04-16 13:05:00',1,'2023-04-16 13:05:00',0),(2673,'rbac:role:update','修改角色',2454,1,'2023-04-16 13:05:11',1,'2023-04-16 13:05:11',0),(2674,'rbac:role:logic-delete','逻辑删除角色',2454,1,'2023-04-16 13:05:24',1,'2023-04-16 13:05:24',0),(2675,'rbac:role:associate-resources','给角色关联资源',2454,1,'2023-04-16 13:05:59',1,'2023-04-16 13:05:59',0),(2680,'rbac:role:associate-permission','给角色关联权限',2454,1,'2023-04-16 13:06:59',1,'2023-04-16 13:06:59',0),(2681,'rbac:role:tree-list','查询角色列表树',2454,1,'2023-04-16 13:07:12',1,'2023-04-16 13:07:12',0),(2682,'rbac:user:create','创建用户',2452,1,'2023-04-16 21:07:33',1,'2023-04-16 21:07:33',0),(2683,'rbac:user:logic-delete','逻辑删除用户',2452,1,'2023-04-16 21:07:46',1,'2023-04-16 21:07:46',0),(2684,'rbac:user:reset-password','重置用户的密码',2452,1,'2023-04-16 21:07:58',1,'2023-04-16 21:07:58',0),(2685,'rbac:user:associate-role','设置用户关联的角色',2452,1,'2023-04-16 21:08:10',1,'2023-04-16 21:08:10',0),(2686,'rbac:user:page','分页查询用户',2452,1,'2023-04-16 21:08:25',1,'2023-04-16 21:08:25',0),(2717,'rbac:custom:logic-delete','逻辑删除客户',2453,1,'2023-04-17 17:24:55',1,'2023-04-17 17:24:55',0),(2718,'rbac:custom:associate-role','设置客户关联的角色',2453,1,'2023-04-17 17:25:09',1,'2023-04-17 17:25:09',0),(2719,'rbac:custom:page','分页查询客户',2453,1,'2023-04-17 17:25:26',1,'2023-04-17 17:25:26',0),(2820,'log:login-log:page','分页查询登录日志',2779,1,'2023-04-18 18:08:33',1,'2023-04-18 18:08:33',0),(2861,'log:audit-log:page','分页查询审计日志',2780,1,'2023-04-20 10:30:11',1,'2023-04-20 10:50:03',0);
/*!40000 ALTER TABLE `garoupa_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_resource`
--

DROP TABLE IF EXISTS `garoupa_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_resource` (
  `id` bigint NOT NULL COMMENT '菜单按钮id',
  `parent_id` bigint NOT NULL COMMENT '上级id，非空；如果没有上级则为0；上级资源只能是菜单',
  `resource_type` int unsigned NOT NULL COMMENT '资源类型，非空；0：菜单，10：按钮',
  `resource_code` char(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源编码，非空；限200字；形如“模块名:菜单名”；唯一',
  `resource_name` char(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称，非空；限200字',
  `icon` char(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标；限250字',
  `route` char(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由；限250字',
  `sort_number` int NOT NULL COMMENT '排序，非空；小号在前',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  KEY `garoupa_resource_resource_code_deleted_tag_index` (`resource_code`,`deleted_tag`),
  KEY `garoupa_resource_parent_id_index` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资源（菜单或按钮）表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_resource`
--

LOCK TABLES `garoupa_resource` WRITE;
/*!40000 ALTER TABLE `garoupa_resource` DISABLE KEYS */;
INSERT INTO `garoupa_resource` VALUES (1,0,0,'system:root','根菜单',NULL,NULL,0,1,'2023-03-30 22:28:57',1,'2023-03-30 22:28:57',0),(2441,0,0,'system-management','系统管理','','',0,1,'2023-04-13 23:50:50',1,'2023-04-14 20:17:32',0),(2452,2441,0,'user-management','用户管理','','',0,1,'2023-04-13 23:53:44',1,'2023-04-13 23:53:44',0),(2453,2441,0,'custom-management','客户管理','','',1,1,'2023-04-13 23:53:54',1,'2023-04-13 23:53:54',0),(2454,2441,0,'role-management','角色管理','','',2,1,'2023-04-13 23:54:19',1,'2023-04-13 23:54:19',0),(2455,2441,0,'menu-management','菜单管理','','',3,1,'2023-04-13 23:54:28',1,'2023-04-13 23:54:28',0),(2456,2441,0,'permission-management','权限管理','','',4,1,'2023-04-13 23:58:11',1,'2023-04-14 20:18:34',0),(2487,2441,0,'system-param-management','系统参数管理','','',5,1,'2023-04-14 20:21:36',1,'2023-04-14 20:21:36',0),(2488,2441,0,'system-dict-management','系统字典管理','','',6,1,'2023-04-14 20:21:49',1,'2023-04-14 20:21:49',0),(2779,2441,0,'login-log','登录日志','','',7,1,'2023-04-18 18:07:25',1,'2023-04-18 18:07:25',0),(2780,2441,0,'audit-log','审计日志','','',8,1,'2023-04-18 18:07:55',1,'2023-04-18 18:07:55',0);
/*!40000 ALTER TABLE `garoupa_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_role`
--

DROP TABLE IF EXISTS `garoupa_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_role` (
  `id` bigint NOT NULL COMMENT '角色id',
  `role_code` char(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码，非空；限50字，小写下划线命名法；唯一',
  `role_name` char(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称，非空；限50字；唯一',
  `parent_id` bigint NOT NULL COMMENT '上级角色id，非空；如果没有上级角色则为0',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `garoupa_role_role_code_deleted_tag_uindex` (`role_code`,`deleted_tag`),
  UNIQUE KEY `garoupa_role_role_name_deleted_tag_uindex` (`role_name`,`deleted_tag`),
  KEY `garoupa_role_parent_id_index` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_role`
--

LOCK TABLES `garoupa_role` WRITE;
/*!40000 ALTER TABLE `garoupa_role` DISABLE KEYS */;
INSERT INTO `garoupa_role` VALUES (88,'super_admin','超级管理员',0,-1,'2023-03-26 16:28:32',-1,'2023-03-26 17:04:06',0),(2999,'system-manager','系统管理员',0,1,'2023-04-15 23:43:49',1,'2023-04-15 23:43:49',0),(3000,'resource-permission-manager','资源权限录入员',2999,1,'2023-04-15 23:44:29',1,'2023-04-15 23:44:29',0);
/*!40000 ALTER TABLE `garoupa_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_role_permission_relation`
--

DROP TABLE IF EXISTS `garoupa_role_permission_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_role_permission_relation` (
  `id` bigint NOT NULL COMMENT '角色权限关系id',
  `role_id` bigint NOT NULL COMMENT '角色id，非空',
  `permission_id` bigint NOT NULL COMMENT '权限id，非空',
  PRIMARY KEY (`id`),
  KEY `garoupa_role_permission_relation_role_id_index` (`role_id`),
  KEY `garoupa_role_permission_relation_permission_id_index` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_role_permission_relation`
--

LOCK TABLES `garoupa_role_permission_relation` WRITE;
/*!40000 ALTER TABLE `garoupa_role_permission_relation` DISABLE KEYS */;
INSERT INTO `garoupa_role_permission_relation` VALUES (1,88,1);
/*!40000 ALTER TABLE `garoupa_role_permission_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_role_resource_relation`
--

DROP TABLE IF EXISTS `garoupa_role_resource_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_role_resource_relation` (
  `id` bigint NOT NULL COMMENT '角色资源关系id',
  `role_id` bigint NOT NULL COMMENT '角色id，非空',
  `resource_id` bigint NOT NULL COMMENT '资源id，非空',
  PRIMARY KEY (`id`),
  KEY `garoupa_role_resource_relation_resource_id_index` (`resource_id`),
  KEY `garoupa_role_resource_relation_role_id_index` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色资源关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_role_resource_relation`
--

LOCK TABLES `garoupa_role_resource_relation` WRITE;
/*!40000 ALTER TABLE `garoupa_role_resource_relation` DISABLE KEYS */;
INSERT INTO `garoupa_role_resource_relation` VALUES (1,88,1);
/*!40000 ALTER TABLE `garoupa_role_resource_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_system_dict`
--

DROP TABLE IF EXISTS `garoupa_system_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_system_dict` (
  `id` bigint NOT NULL COMMENT '系统字典id',
  `dict_code` char(250) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典编码，非空；限250字，小写下划线命名法',
  `dict_name` char(250) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称，非空；限250字',
  `dict_describe` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典描述；限1000字',
  `sort_number` int NOT NULL COMMENT '排序，非空；小号在前',
  `lock_sign` tinyint(1) NOT NULL COMMENT '锁定标识，非空；false：未锁定，true：已锁定',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `garoupa_system_dict_dict_code_deleted_tag_uindex` (`dict_code`,`deleted_tag`),
  FULLTEXT KEY `garoupa_system_dict_dict_code_fulltext_index` (`dict_code`) /*!50100 WITH PARSER `ngram` */ ,
  FULLTEXT KEY `garoupa_system_dict_dict_name_fulltext_index` (`dict_name`) /*!50100 WITH PARSER `ngram` */ 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_system_dict`
--

LOCK TABLES `garoupa_system_dict` WRITE;
/*!40000 ALTER TABLE `garoupa_system_dict` DISABLE KEYS */;
INSERT INTO `garoupa_system_dict` VALUES (16,'lock_sign','锁定标识','',0,1,1,'2023-04-12 16:15:56',1,'2023-04-20 09:45:32',0),(17,'effective_status','有效状态','',0,1,1,'2023-04-12 16:17:14',1,'2023-04-20 09:46:29',0),(98,'rbac_resource_type','rbac资源类型','rbac权限模型中的资源，本系统指菜单或按钮',1,1,1,'2023-04-13 15:50:28',1,'2023-04-20 09:46:33',0),(309,'rbac_user_type','rbac用户类型','用户表示管理端用户，客户表示客户端用户',1,1,1,'2023-04-16 13:30:26',1,'2023-04-20 09:46:38',0),(340,'gender','性别',NULL,0,1,1,'2023-04-17 16:27:52',1,'2023-04-20 09:46:42',0),(431,'true_false','是否','',0,1,1,'2023-04-18 17:06:40',1,'2023-04-20 09:46:46',0),(462,'audit_log_log_type','审计日志日志类型',NULL,2,1,1,'2023-04-20 09:27:33',1,'2023-04-20 09:46:51',0);
/*!40000 ALTER TABLE `garoupa_system_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_system_dict_item`
--

DROP TABLE IF EXISTS `garoupa_system_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_system_dict_item` (
  `id` bigint NOT NULL COMMENT '系统字典项id',
  `dict_id` bigint NOT NULL COMMENT '系统字典id，非空',
  `dict_code` char(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '系统字典编码，冗余；限250字，小写下划线命名法',
  `dict_name` char(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '系统字典名称，冗余；限250字',
  `item_value` char(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项值，非空；限50字',
  `item_text` char(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项文本，非空；限50字',
  `sort_number` int NOT NULL COMMENT '排序，非空；小号在前',
  `effective_status` int NOT NULL COMMENT '有效状态，非空；0：无效，10：有效',
  `lock_sign` tinyint(1) NOT NULL COMMENT '锁定标识，非空；false；未锁定，true：已锁定',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `garoupa_system_dict_item_dict_id_item_value_deleted_tag_uindex` (`dict_id`,`item_value`,`deleted_tag`),
  KEY `garoupa_system_dict_item_dict_code_item_value_index` (`dict_code`,`item_value`),
  KEY `garoupa_system_dict_item_dict_id_sort_number_create_time_index` (`dict_id`,`sort_number`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统字典项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_system_dict_item`
--

LOCK TABLES `garoupa_system_dict_item` WRITE;
/*!40000 ALTER TABLE `garoupa_system_dict_item` DISABLE KEYS */;
INSERT INTO `garoupa_system_dict_item` VALUES (11,16,'lock_sign','锁定标识','false','未锁定',0,10,0,1,'2023-04-12 16:16:27',1,'2023-04-12 16:16:27',0),(12,16,'lock_sign','锁定标识','true','已锁定',1,10,0,1,'2023-04-12 16:16:35',1,'2023-04-12 16:16:35',0),(13,17,'effective_status','有效状态','0','无效',0,10,0,1,'2023-04-12 16:17:28',1,'2023-04-12 16:17:28',0),(14,17,'effective_status','有效状态','10','有效',10,10,0,1,'2023-04-12 16:17:38',1,'2023-04-12 16:17:38',0),(95,98,'rbac_resource_type','rbac资源类型','0','菜单',0,10,0,1,'2023-04-13 15:51:27',1,'2023-04-13 15:51:27',0),(96,98,'rbac_resource_type','rbac资源类型','10','按钮',10,10,0,1,'2023-04-13 15:51:32',1,'2023-04-13 15:51:32',0),(307,309,'rbac_user_type','rbac用户类型','0','用户',0,10,0,1,'2023-04-16 13:30:53',1,'2023-04-16 13:30:53',0),(308,309,'rbac_user_type','rbac用户类型','10','客户',10,10,0,1,'2023-04-16 13:30:58',1,'2023-04-16 13:30:58',0),(339,340,'gender','性别','0','男',0,10,0,1,'2023-04-17 16:28:32',1,'2023-04-17 16:28:32',0),(340,340,'gender','性别','10','女',10,10,0,1,'2023-04-17 16:28:38',1,'2023-04-17 16:28:38',0),(341,340,'gender','性别','20','保密',20,10,0,1,'2023-04-17 16:28:45',1,'2023-04-17 16:28:45',0),(432,431,'true_false','是否','false','否',0,10,0,1,'2023-04-18 17:07:15',1,'2023-04-18 17:07:15',0),(433,431,'true_false','是否','true','是',1,10,0,1,'2023-04-18 17:07:39',1,'2023-04-18 17:07:39',0),(464,462,'audit_log_log_type','审计日志日志类型','0','正常',0,10,0,1,'2023-04-20 09:28:00',1,'2023-04-20 09:28:00',0),(465,462,'audit_log_log_type','审计日志日志类型','10','错误',10,10,0,1,'2023-04-20 09:28:05',1,'2023-04-20 09:28:05',0);
/*!40000 ALTER TABLE `garoupa_system_dict_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_system_param`
--

DROP TABLE IF EXISTS `garoupa_system_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_system_param` (
  `id` bigint NOT NULL COMMENT '系统参数id',
  `param_category_id` bigint NOT NULL COMMENT '参数分类id，非空',
  `param_code` char(250) COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数编码，非空，唯一；小驼峰命名法，限250字',
  `param_name` char(250) COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名称，非空，唯一；限250字',
  `param_value` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数值，非空；限2000字',
  `lock_sign` tinyint(1) NOT NULL COMMENT '锁定标识，如果是锁定的系统参数，则不允许删除，非空；false：未锁定，true：锁定',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `garoupa_system_param_param_code_deleted_tag_uindex` (`param_code`,`deleted_tag`),
  UNIQUE KEY `garoupa_system_param_param_name_deleted_tag_uindex` (`param_name`,`deleted_tag`),
  FULLTEXT KEY `garoupa_system_param_param_code_fulltext_index` (`param_code`) /*!50100 WITH PARSER `ngram` */ ,
  FULLTEXT KEY `garoupa_system_param_param_name_fulltext_index` (`param_name`) /*!50100 WITH PARSER `ngram` */ 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_system_param`
--

LOCK TABLES `garoupa_system_param` WRITE;
/*!40000 ALTER TABLE `garoupa_system_param` DISABLE KEYS */;
INSERT INTO `garoupa_system_param` VALUES (381,13,'jwt.expireSecond','Jwt过期时间（单位秒）','2592000',1,1,'2023-04-07 23:48:06',1,'2023-04-12 23:30:50',0),(842,866,'rbac.userDefaultPassword','用户默认密码','Garoupa2023',0,1,'2023-04-16 13:34:33',1,'2023-04-16 13:34:33',0),(843,866,'rbac.userDefaultNicknamePrefix','用户默认昵称前缀','管理员',0,1,'2023-04-16 13:49:13',1,'2023-04-16 13:49:13',0),(844,866,'rbac.userPasswordValidRegularExpression','用户密码校验正则表达式','^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$',0,1,'2023-04-16 20:41:20',1,'2023-04-16 20:41:20',0),(845,866,'rbac.userPasswordValidRegularExpressionTextRequirement','用户密码校验正则表达式文字要求','密码至少包含一个大写字母，一个小写字母，一个数字，长度8-50位',0,1,'2023-04-16 21:49:13',1,'2023-04-16 21:49:13',0);
/*!40000 ALTER TABLE `garoupa_system_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_system_param_category`
--

DROP TABLE IF EXISTS `garoupa_system_param_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_system_param_category` (
  `id` bigint NOT NULL COMMENT '系统参数分类id',
  `category_name` char(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统参数分类名称，非空，唯一；限50字',
  `lock_sign` tinyint(1) NOT NULL COMMENT '锁定标识，如果是锁定的系统参数分类，则不允许删除，非空；false：未锁定，true：锁定',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `garoupa_system_param_category_category_name_deleted_tag_uindex` (`category_name`,`deleted_tag`),
  KEY `garoupa_system_param_category_create_time_index` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统参数分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_system_param_category`
--

LOCK TABLES `garoupa_system_param_category` WRITE;
/*!40000 ALTER TABLE `garoupa_system_param_category` DISABLE KEYS */;
INSERT INTO `garoupa_system_param_category` VALUES (13,'JWT参数',1,1,'2023-04-06 22:05:38',1,'2023-04-08 00:00:33',0),(866,'RBAC参数',0,1,'2023-04-16 13:33:53',1,'2023-04-16 13:33:53',0);
/*!40000 ALTER TABLE `garoupa_system_param_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_user`
--

DROP TABLE IF EXISTS `garoupa_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_user` (
  `id` bigint NOT NULL COMMENT '用户id',
  `user_name` char(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，非空；限50字；唯一',
  `password` char(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '散列后的密码，非空',
  `nick_name` char(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称，非空；限50字',
  `avatar_uri` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像uri；限2000字',
  `real_name` char(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户真实姓名；限50字',
  `id_card_number` char(18) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户身份证号；限18字',
  `mobile` char(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电话号码；限50字',
  `email` char(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱；限50字',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `gender` int unsigned DEFAULT NULL COMMENT '性别；0：男，10：女，20：未知',
  `user_type` int unsigned NOT NULL COMMENT '用户类型，非空；0：用户，10：客户',
  `create_user` bigint NOT NULL COMMENT '创建人id，非空',
  `create_time` datetime NOT NULL COMMENT '创建时间，非空',
  `update_user` bigint NOT NULL COMMENT '最后修改人id，非空',
  `update_time` datetime NOT NULL COMMENT '最后修改时间，非空',
  `deleted_tag` bigint NOT NULL COMMENT '逻辑删除标识，非空；0：未删除，其他：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `garoupa_user_user_name_deleted_tag_uindex` (`user_name`,`deleted_tag`),
  UNIQUE KEY `garoupa_user_email_deleted_tag_uindex` (`email`,`deleted_tag`),
  KEY `garoupa_user_email_create_time_index` (`email`,`create_time`),
  KEY `garoupa_user_id_card_number_create_time_index` (`id_card_number`,`create_time`),
  KEY `garoupa_user_mobile_create_time_index` (`mobile`,`create_time`),
  FULLTEXT KEY `garoupa_user_user_name_fulltext_index` (`user_name`) /*!50100 WITH PARSER `ngram` */ ,
  FULLTEXT KEY `garoupa_user_nick_name_fulltext_index` (`nick_name`) /*!50100 WITH PARSER `ngram` */ 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_user`
--

LOCK TABLES `garoupa_user` WRITE;
/*!40000 ALTER TABLE `garoupa_user` DISABLE KEYS */;
INSERT INTO `garoupa_user` VALUES (1,'Garoupa','$2a$10$K3Lfz2LP4.ycX1ROcfsjxu1unlhe6xjiWuFw14IXtS576ZTTwvrsC','garoupa',NULL,NULL,NULL,NULL,'670034105@qq.com',NULL,NULL,0,1,'2023-03-29 10:54:11',1,'2023-03-29 10:54:11',0);
/*!40000 ALTER TABLE `garoupa_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garoupa_user_role_relation`
--

DROP TABLE IF EXISTS `garoupa_user_role_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garoupa_user_role_relation` (
  `id` bigint NOT NULL COMMENT '用户角色关系表',
  `user_id` bigint NOT NULL COMMENT '用户id，非空',
  `role_id` bigint NOT NULL COMMENT '角色id，非空',
  PRIMARY KEY (`id`),
  KEY `garoupa_user_role_relation_user_id_index` (`user_id`),
  KEY `garoupa_user_role_relation_role_id_index` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garoupa_user_role_relation`
--

LOCK TABLES `garoupa_user_role_relation` WRITE;
/*!40000 ALTER TABLE `garoupa_user_role_relation` DISABLE KEYS */;
INSERT INTO `garoupa_user_role_relation` VALUES (1,1,88);
/*!40000 ALTER TABLE `garoupa_user_role_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-20 23:57:02
