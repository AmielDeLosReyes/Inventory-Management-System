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
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_variation_id` int DEFAULT NULL,
  `quantity_sold` int DEFAULT NULL,
  `total_revenue` decimal(38,2) DEFAULT NULL,
  `total_cost` decimal(38,2) DEFAULT NULL,
  `total_profit` decimal(38,2) DEFAULT NULL,
  `transaction_date` datetime(6) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `is_refund` bit(1) DEFAULT NULL,
  `quantity_refunded` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_variation_id` (`product_variation_id`),
  KEY `FK5bgaw8g0rrbqdvafq36g58smk` (`user_id`),
  CONSTRAINT `FK5bgaw8g0rrbqdvafq36g58smk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`product_variation_id`) REFERENCES `product_variation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (88,46,2,339.98,199.98,140.00,'2024-04-10 18:54:33.682435',1,_binary '\0',2),(89,46,-2,-339.98,-199.98,-140.00,'2024-04-10 18:54:45.706883',1,_binary '',NULL),(90,46,5,849.95,499.95,350.00,'2024-04-12 15:15:57.842624',1,_binary '\0',0),(91,49,3,509.97,299.97,210.00,'2024-04-12 15:17:21.238071',1,_binary '\0',0),(92,46,2,339.98,199.98,140.00,'2024-04-14 17:45:35.727319',1,_binary '\0',0);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-14 20:21:41
