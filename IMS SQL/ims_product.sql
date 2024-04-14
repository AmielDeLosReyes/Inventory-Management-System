-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: ims
-- ------------------------------------------------------
-- Server version	8.1.0

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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `product_desc` text,
  `cost_price` decimal(10,2) DEFAULT NULL,
  `selling_price` decimal(10,2) DEFAULT NULL,
  `image_urls` varchar(10000) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK47nyv78b35eaufr6aa96vep6n` (`user_id`),
  CONSTRAINT `FK47nyv78b35eaufr6aa96vep6n` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (34,'2Pac Vintage Tee','2Pac vintage T-Shirt. EDITED',99.99,169.99,'[/images/2Pac Vintage Tee/2pac.jpg]',1,_binary '\0'),(38,'Lil Baby Vintage Tee','Lil Baby Vintage Tee',79.99,189.99,'[/images/Lil Baby Vintage Tee/lilbaby.jpg]',1,_binary ''),(39,'hahahaha','hahahaha',1234.00,12345.00,'[/images/hahahaha/work4-9.jpg, /images/hahahaha/work2-1.png]',1,_binary ''),(40,'hahahaha','hahahaha',1234.00,12345.00,'[/images/hahahaha/work4-9.jpg, /images/hahahaha/work2-1.png]',1,_binary ''),(41,'jsjsjsjs','jsjsjsjs',1234.00,12345.00,'[/images/jsjsjsjs/work4-9.jpg, /images/jsjsjsjs/work2-1.png]',1,_binary ''),(42,'Edited Name','Edits the name',69.99,89.99,'[/images/dgsasd/work4-9.jpg, /images/dgsasd/work-4.jpg]',1,_binary '\0');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-14 20:21:36
