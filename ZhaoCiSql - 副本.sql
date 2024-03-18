-- MySQL dump 10.13  Distrib 8.3.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: chaoci
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `blacked`
--

DROP TABLE IF EXISTS `blacked`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blacked` (
  `user_uid` int NOT NULL COMMENT '用户账号',
  `object_uid` int NOT NULL COMMENT '被屏蔽的对象的uid',
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户屏蔽的对象';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blacked`
--

LOCK TABLES `blacked` WRITE;
/*!40000 ALTER TABLE `blacked` DISABLE KEYS */;
/*!40000 ALTER TABLE `blacked` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cron`
--

DROP TABLE IF EXISTS `cron`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cron` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `email_id` int NOT NULL COMMENT '要设为可见的信件id',
  `execute_time` datetime NOT NULL COMMENT '执行时刻',
  `start_time` date NOT NULL COMMENT '执行时间所在年的第一天',
  `dead_time` date NOT NULL COMMENT '执行时间所在年的最后一天',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='延时信件处理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cron`
--

LOCK TABLES `cron` WRITE;
/*!40000 ALTER TABLE `cron` DISABLE KEYS */;
/*!40000 ALTER TABLE `cron` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_groups`
--

DROP TABLE IF EXISTS `friend_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_groups` (
  `uid` int DEFAULT NULL COMMENT '创建分组的用户',
  `group` varchar(20) NOT NULL COMMENT '分组',
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_groups`
--

LOCK TABLES `friend_groups` WRITE;
/*!40000 ALTER TABLE `friend_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_relationship`
--

DROP TABLE IF EXISTS `friend_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_relationship` (
  `requested_uid` int NOT NULL COMMENT '发出好友关系绑定方',
  `requester_uid` int NOT NULL COMMENT '接受好友关系绑定方',
  `type` tinyint NOT NULL COMMENT '关系类型1,基友，2，闺蜜，3.情侣',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '是否绑定，0不绑定，1绑定'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_relationship`
--

LOCK TABLES `friend_relationship` WRITE;
/*!40000 ALTER TABLE `friend_relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend_relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_request`
--

DROP TABLE IF EXISTS `friend_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_request` (
  `requested_uid` int NOT NULL COMMENT '接受请求的用户的uid',
  `requester_uid` int NOT NULL COMMENT '发送请求用户的uid',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '状态，1同意，0不同意',
  `create_time` datetime NOT NULL COMMENT '请求时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='好友请求';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_request`
--

LOCK TABLES `friend_request` WRITE;
/*!40000 ALTER TABLE `friend_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friends` (
  `user_uid` int NOT NULL,
  `friend_uid` int DEFAULT NULL,
  `remark` varchar(20) DEFAULT NULL COMMENT 'uid1给uid2设置的备注',
  `group_id` int DEFAULT NULL COMMENT 'uid1给uid2设置的分组',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除0表示不删除，1表示删除',
  `blacked` tinyint NOT NULL DEFAULT '0' COMMENT '表示是否屏蔽好友，0表示不屏蔽，1表示屏蔽',
  `relation` varchar(20) DEFAULT NULL COMMENT '关系',
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='好友关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_email`
--

DROP TABLE IF EXISTS `tb_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_email` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text COMMENT '文本内容',
  `sender_uid` int NOT NULL COMMENT '发件人',
  `addressee_uid` int NOT NULL COMMENT '收件人uid',
  `send_time` datetime NOT NULL COMMENT '发件时间',
  `read` tinyint NOT NULL DEFAULT '0' COMMENT '已读？0未读；1已读',
  `deleted_sender` tinyint NOT NULL DEFAULT '0' COMMENT '发件是否删除信件,0不删除，1删除',
  `deleted_addressee` tinyint DEFAULT '0' COMMENT '收件方是否删除信件,0不删除，1删除',
  `title` varchar(20) NOT NULL COMMENT '信件标题',
  `group_sender` varchar(20) DEFAULT NULL COMMENT '发件人创建的分组',
  `group_addressee` varchar(20) DEFAULT NULL COMMENT '收件人创建的分组',
  `inbox_top` tinyint DEFAULT '0' COMMENT '收件方置顶？0不置顶；1置顶',
  `visibility` tinyint DEFAULT '0' COMMENT '可见性?0可见1不可见',
  `outbox_top` tinyint DEFAULT '0' COMMENT '发件方置顶？0不置顶；1置顶',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_email`
--

LOCK TABLES `tb_email` WRITE;
/*!40000 ALTER TABLE `tb_email` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_email` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `username` varchar(20) NOT NULL COMMENT '用户们',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `uid` int NOT NULL AUTO_INCREMENT,
  `inbox_group` varchar(1000) DEFAULT NULL COMMENT '收件箱分组',
  `outbox_group` varchar(1000) DEFAULT NULL COMMENT '发件箱分组',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `User_pk` (`username`),
  UNIQUE KEY `User_pk_2` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2046 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-26 20:05:13
