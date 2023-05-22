-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: k8a806.p.ssafy.io    Database: ssafy806_payment
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
-- Table structure for table `BATCH_JOB_EXECUTION_PARAMS`
--

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_PARAMS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BATCH_JOB_EXECUTION_PARAMS` (
  `JOB_EXECUTION_ID` bigint NOT NULL,
  `PARAMETER_NAME` varchar(100) NOT NULL,
  `PARAMETER_TYPE` varchar(100) NOT NULL,
  `PARAMETER_VALUE` varchar(2500) DEFAULT NULL,
  `IDENTIFYING` char(1) NOT NULL,
  KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_JOB_EXECUTION_PARAMS`
--

LOCK TABLES `BATCH_JOB_EXECUTION_PARAMS` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_PARAMS` DISABLE KEYS */;
INSERT INTO `BATCH_JOB_EXECUTION_PARAMS` VALUES (1,'name','java.lang.String','5','Y'),(2,'name','java.lang.String','5','Y'),(3,'name','java.lang.String','5','Y'),(4,'name','java.lang.String','5','Y'),(5,'name','java.lang.String','5','Y'),(6,'name','java.lang.String','5','Y'),(7,'name','java.lang.String','5','Y'),(8,'name','java.lang.String','5','Y'),(9,'name','java.lang.String','5','Y'),(10,'name','java.lang.String','5','Y'),(11,'name','java.lang.String','5','Y'),(12,'name','java.lang.String','5','Y'),(13,'batch','java.lang.String','20230508','Y'),(14,'batchDate','java.lang.String','20230508','Y'),(15,'batchDate','java.lang.String','20230508','Y'),(16,'batchDate','java.lang.String','20230508','Y'),(19,'batchDate','java.lang.String','20230510','Y'),(22,'batchDate','java.lang.String','20230512','Y'),(23,'batchDate','java.lang.String','20230512','Y'),(25,'batchDate','java.lang.String','20230513','Y'),(26,'batchDate','java.lang.String','20230513','Y'),(28,'batchDate','java.lang.String','20230514','Y'),(29,'batchDate','java.lang.String','20230514','Y'),(31,'batchDate','java.lang.String','20230515','Y'),(32,'batchDate','java.lang.String','20230515','Y'),(33,'batchDate','java.lang.String','20230515','Y'),(34,'batchDate','java.lang.String','20230515','Y'),(35,'batchDate','java.lang.String','20230515','Y'),(36,'batchDate','java.lang.String','20230515','Y'),(37,'batchDate','java.lang.String','20230516','Y'),(38,'batchDate','java.lang.String','20230516','Y'),(39,'batchDate','java.lang.String','20230517','Y'),(40,'batchDate','java.lang.String','20230517','Y'),(41,'batchDate','java.lang.String','20230518','Y'),(42,'batchDate','java.lang.String','20230518','Y'),(43,'batchDate','java.lang.String','20230518','Y');
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_PARAMS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-19  9:34:12
