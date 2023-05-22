-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: k8a806.p.ssafy.io    Database: ssafy806_user
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `modified_date` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `provider` varchar(20) NOT NULL,
  `role` varchar(20) NOT NULL,
  `uuid` varchar(30) DEFAULT NULL,
  `passbook_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_b1uekjbsmgj2cosrue40fo1a9` (`passbook_id`),
  CONSTRAINT `FK3a5mw3flm5f3iu0rmy2fe3ijo` FOREIGN KEY (`passbook_id`) REFERENCES `passbook` (`passbook_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2023-05-08 17:23:57.554636','2023-05-08 17:23:57.554636','159632_@naver.com','성원준','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','KAKAO','ROLE_USER','2766882173',1),(2,'2023-05-09 00:18:59.025889','2023-05-09 00:18:59.025889','kjskjs356@naver.com','김정수','http://k.kakaocdn.net/dn/6DmUo/btr1MjSwZds/YKPDEKnIuYLj6idpldbDL0/img_640x640.jpg','KAKAO','ROLE_USER','2770333690',2),(3,'2023-05-09 00:41:15.016207','2023-05-09 00:41:15.016207','jn307742@naver.com','최준아','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','KAKAO','ROLE_USER','2781560411',3),(4,'2023-05-12 05:45:49.292784','2023-05-12 05:45:49.292784','yoonhosan@naver.com','윤호산','http://k.kakaocdn.net/dn/yURgF/btrLbv8dbQx/6rM9P9NVh3y29M2LBx6iP1/img_640x640.jpg','KAKAO','ROLE_USER','2786863328',4),(5,'2023-05-17 02:03:24.215357','2023-05-17 02:03:24.215357','rlaehddus815@gmail.com','동연','http://k.kakaocdn.net/dn/bVi2Cc/btr3Svv4shw/Dppi2t7xcSAJuF5JO2v6S0/img_640x640.jpg','KAKAO','ROLE_USER','2793549937',5);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-19  9:31:22
