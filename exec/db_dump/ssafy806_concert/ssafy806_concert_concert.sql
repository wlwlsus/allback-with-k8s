-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: k8a806.p.ssafy.io    Database: ssafy806_concert
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
-- Table structure for table `concert`
--

DROP TABLE IF EXISTS `concert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `concert` (
  `concert_id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `modified_date` datetime(6) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  `end_date` datetime(6) NOT NULL,
  `image` varchar(1000) NOT NULL,
  `stage_id` bigint NOT NULL,
  `start_date` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`concert_id`),
  KEY `FK3rn7p9exg7s33y08tv6pkdig3` (`stage_id`),
  CONSTRAINT `FK3rn7p9exg7s33y08tv6pkdig3` FOREIGN KEY (`stage_id`) REFERENCES `stage` (`stage_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concert`
--

LOCK TABLES `concert` WRITE;
/*!40000 ALTER TABLE `concert` DISABLE KEYS */;
INSERT INTO `concert` VALUES (1,'2023-05-07 16:50:18.000000','2023-05-07 16:50:18.000000','국내 유일의 복합문화 예술 페스티벌','2023-06-17 16:50:18.000000','https://mblogthumb-phinf.pstatic.net/MjAyMzA0MjFfNjcg/MDAxNjgyMDYzNzg1NTkx.Ku1v91C_JtUr7Qecjz2NtwTxakZSmtDnBRu08Su0L7wg.Li_1APCUKOBstRtG8NZGAdRr-Eido4qHP-BVqJC_wzAg.PNG.kssmadang/1.png?type=w800',1,'2023-06-10 16:50:18.000000','상상실현페스티벌',1),(3,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','케이시 데뷔 8주년 여름콘서트','2023-06-17 00:00:00.007000','https://cdn.nbnnews.co.kr/news/photo/202305/760705_767195_2927.jpg',1,'2023-05-10 16:50:18.000000','케이시 콘서트 <여덟번째 여름>',1),(4,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','라쿠나의 우주에 여러분을 초대해요．','2023-06-10 16:50:18.000000','https://cdn.imweb.me/upload/S20200106a105fd03f4b57/889906b87d1dd.jpg',1,'2023-06-10 16:50:18.000000','먼데이프로젝트 시즌6 Our Universe',1),(5,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','박규리가 직접 준비한 생일파티에 함께하고 싶다면？','2023-06-10 16:50:18.000000','https://cdn.topstarnews.net/news/photo/202305/15334492_1113358_4217.jpg',1,'2023-06-10 16:50:18.000000','박규리 팬미팅 〈HAPPY GYURI DAY〉',1),(6,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','살아있는 음악, 우리만의 뜨거운 축제','2023-06-10 16:50:18.000000','http://tkfile.yes24.com/upload2/PerfBlog/202304/20230403/20230403-45499_1.jpg',1,'2023-06-10 16:50:18.000000','PEAK FESTIVAL 2023',1),(7,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','세상을 바꾼 기적의 음악, 그 위대한 감동','2023-06-10 16:50:18.000000','https://i.namu.wiki/i/K5DuQwQntrF5V4Rh9EfHDGHcOmnuZjDfsL_IJAq3ZLprGA0Mu23LMS5e0_3ZJX-SSremVbIkI8YNqtWa7BkzOgGHCiX4nHot3Tn-mNw3lBhL0tU9gTCXXzBhnxC52oHXcOzCZvl-drdhwLOObQzomw.webp',1,'2023-06-10 16:50:18.000000','뮤지컬 <모차르트!>',1),(8,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','현대인에게 전하는 날카로운 질문과 우아한 담론','2023-06-10 16:50:18.000000','https://cdn.newsculture.press/news/photo/202301/514846_630196_353.jpg',1,'2023-06-10 16:50:18.000000','연극 〈테베랜드〉',1),(9,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','베토벤의 원곡들에 기반하고 실화에서 영감을 받다.','2023-05-17 16:50:18.000000','http://dancetv.kr/Files/175/News/202303/3228_20230316234732097.JPG',1,'2023-05-10 16:50:18.000000','뮤지컬 〈베토벤；Beethoven Secret〉',1),(10,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','서로 다른 5인 남녀의 연애레시피','2023-06-10 16:50:18.000000','https://image.edaily.co.kr/images/photo/files/NP/S/2016/02/PS16021600150.jpg\n',1,'2023-06-10 16:50:18.000000','［서울 대학로］연극 라면',1),(12,'2023-05-12 14:46:32.000000','2023-05-07 16:50:18.000000','〈빨래〉는 오늘을 살아가는 우리들의 이야기다','2023-06-10 16:50:18.000000','http://tkfile.yes24.com/upload2/PerfBlog/202109/20210929/20210929-40280.jpg',1,'2023-06-10 16:50:18.000000','뮤지컬 〈빨래〉',1),(25,'2023-05-16 13:27:07.000000','2023-05-16 13:27:09.000000','2023 IU CONCERT 〈The Golden Hour：오렌지 태양 아래〉\n\n','2023-06-20 13:29:21.000000','https://www.thedrive.co.kr/news/data/20220811/p1065595094835127_500_thum.png',1,'2023-06-16 13:31:56.000000','2023 IU CONCERT 〈The Golden Hour：오렌지 태양 아래〉',1),(26,'2023-05-16 13:34:12.000000','2023-05-16 13:34:13.000000','러브 라이브! 선샤인!! 서울 공연','2023-05-04 13:33:40.000000','	https://cdn.newsworks.co.kr/news/photo/201904/352412_248841_453.jpg',1,'2023-04-16 13:34:19.000000','러브 라이브! 선샤인!! 서울 공연',1);
/*!40000 ALTER TABLE `concert` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-19  9:35:32
