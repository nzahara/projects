-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: project_schema
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `availability_timigs`
--

DROP TABLE IF EXISTS `availability_timigs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `availability_timigs` (
  `tutor_availability_id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_id` int(11) NOT NULL,
  `service_type_id` int(11) NOT NULL,
  `week_day` varchar(50) NOT NULL,
  `avail_time_from` datetime NOT NULL,
  `avail_time_to` datetime NOT NULL,
  PRIMARY KEY (`tutor_availability_id`),
  UNIQUE KEY `tutor_availability_id` (`tutor_availability_id`),
  KEY `schedule_id` (`schedule_id`),
  KEY `service_type_id` (`service_type_id`),
  CONSTRAINT `availability_timigs_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `tutor_schedule` (`schedule_id`),
  CONSTRAINT `availability_timigs_ibfk_2` FOREIGN KEY (`service_type_id`) REFERENCES `services_types` (`service_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `availability_timigs`
--

LOCK TABLES `availability_timigs` WRITE;
/*!40000 ALTER TABLE `availability_timigs` DISABLE KEYS */;
INSERT INTO `availability_timigs` VALUES (1,33,1,'Monday','2010-00-00 00:00:00','2011-00-00 00:00:00'),(2,34,1,'Monday','2010-00-00 00:00:00','2011-00-00 00:00:00'),(3,35,1,'TUESDAY','2011-00-00 00:00:00','2012-00-00 00:00:00'),(4,36,1,'WEDNESDAY','2012-00-00 00:00:00','2013-00-00 00:00:00'),(5,37,1,'THURSDAY','2014-00-00 00:00:00','2015-00-00 00:00:00'),(6,39,1,'Monday','2011-00-00 00:00:00','2012-00-00 00:00:00'),(7,40,1,'TUESDAY','2012-00-00 00:00:00','2013-00-00 00:00:00'),(8,41,1,'WEDNESDAY','2013-00-00 00:00:00','2014-00-00 00:00:00'),(9,38,1,'FRIDAY','2016-00-00 00:00:00','2017-00-00 00:00:00'),(10,42,1,'WEDNESDAY','2013-00-00 00:00:00','2014-00-00 00:00:00'),(11,43,1,'WEDNESDAY','2013-00-00 00:00:00','2014-00-00 00:00:00');
/*!40000 ALTER TABLE `availability_timigs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_type` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'SCIENCE'),(2,'COMMERCE'),(3,'GEOGRAPHY'),(4,'POLITICAL SCIENCE'),(5,'MUSIC');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `gettutorservicedetails`
--

DROP TABLE IF EXISTS `gettutorservicedetails`;
/*!50001 DROP VIEW IF EXISTS `gettutorservicedetails`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `gettutorservicedetails` AS SELECT 
 1 AS `topic_name`,
 1 AS `subject_name`,
 1 AS `category_type`,
 1 AS `avail_time_from`,
 1 AS `avail_time_to`,
 1 AS `service_type`,
 1 AS `class_size`,
 1 AS `tutor_availability_id`,
 1 AS `service_status`,
 1 AS `service_id`,
 1 AS `user_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `searchservices`
--

DROP TABLE IF EXISTS `searchservices`;
/*!50001 DROP VIEW IF EXISTS `searchservices`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `searchservices` AS SELECT 
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `subject_name`,
 1 AS `subject_id`,
 1 AS `topic_id`,
 1 AS `topic_name`,
 1 AS `tutor_availability_id`,
 1 AS `zip_code`,
 1 AS `service_id`,
 1 AS `service_status`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `services_available`
--

DROP TABLE IF EXISTS `services_available`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `services_available` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `tutor_availability_id` int(11) NOT NULL,
  `service_status` tinyint(1) DEFAULT '2',
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_id` (`service_id`),
  KEY `user_id` (`user_id`),
  KEY `topic_id` (`topic_id`),
  KEY `tutor_availability_id` (`tutor_availability_id`),
  CONSTRAINT `services_available_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`topic_id`),
  CONSTRAINT `services_available_ibfk_3` FOREIGN KEY (`tutor_availability_id`) REFERENCES `availability_timigs` (`tutor_availability_id`),
  CONSTRAINT `services_available_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `tutor_role` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services_available`
--

LOCK TABLES `services_available` WRITE;
/*!40000 ALTER TABLE `services_available` DISABLE KEYS */;
INSERT INTO `services_available` VALUES (1,6,1,1,1),(2,6,2,2,1),(3,6,3,3,1),(4,6,4,4,1),(5,6,5,5,1),(6,7,7,6,1),(7,7,8,7,1),(8,7,9,8,1),(9,7,10,9,1),(10,7,11,10,1);
/*!40000 ALTER TABLE `services_available` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services_types`
--

DROP TABLE IF EXISTS `services_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `services_types` (
  `service_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `service_type` varchar(255) NOT NULL,
  PRIMARY KEY (`service_type_id`),
  UNIQUE KEY `service_type_id` (`service_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services_types`
--

LOCK TABLES `services_types` WRITE;
/*!40000 ALTER TABLE `services_types` DISABLE KEYS */;
INSERT INTO `services_types` VALUES (1,'ONLINE'),(2,'OFFLINE'),(3,'GROUP STUDY');
/*!40000 ALTER TABLE `services_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_role`
--

DROP TABLE IF EXISTS `student_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `student_role` (
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `student_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_role`
--

LOCK TABLES `student_role` WRITE;
/*!40000 ALTER TABLE `student_role` DISABLE KEYS */;
INSERT INTO `student_role` VALUES (1),(2),(3),(4),(5);
/*!40000 ALTER TABLE `student_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_services`
--

DROP TABLE IF EXISTS `student_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `student_services` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `service_id` int(11) NOT NULL DEFAULT '0',
  `paid_status` varchar(25) DEFAULT 'NOT PAID',
  `availabilty_status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`,`service_id`),
  KEY `service_id` (`service_id`),
  CONSTRAINT `student_services_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `services_available` (`service_id`),
  CONSTRAINT `student_services_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `student_role` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_services`
--

LOCK TABLES `student_services` WRITE;
/*!40000 ALTER TABLE `student_services` DISABLE KEYS */;
INSERT INTO `student_services` VALUES (1,1,'NOT PAID',0),(1,2,'NOT PAID',0),(2,3,'NOT PAID',0),(2,4,'NOT PAID',0),(3,5,'NOT PAID',0),(3,6,'NOT PAID',0),(4,7,'NOT PAID',0),(4,8,'NOT PAID',0),(5,9,'NOT PAID',0),(5,10,'NOT PAID',0);
/*!40000 ALTER TABLE `student_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `subjects` (
  `subject_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `subject_name` varchar(255) NOT NULL,
  PRIMARY KEY (`subject_id`),
  UNIQUE KEY `subject_id` (`subject_id`),
  KEY `category_id` (`category_id`),
  KEY `subjectName` (`subject_name`),
  CONSTRAINT `subjects_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjects`
--

LOCK TABLES `subjects` WRITE;
/*!40000 ALTER TABLE `subjects` DISABLE KEYS */;
INSERT INTO `subjects` VALUES (1,1,'PHYSICS'),(2,1,'CHEMISTRY'),(3,1,'MATHS'),(4,1,'APPLIED MATHS'),(5,1,'APPLIED PHYSICS'),(6,2,'ACCOUNTING'),(7,2,'FINANCE MANAGEMNT'),(8,2,'BUSINESS MANAGEMENT'),(9,3,'SPATIAL SCIENCE'),(10,3,'EARTH SCEINCE'),(11,4,'CONSTITUTION'),(12,4,'CIVIL LAWS'),(13,5,'CLASSICAL'),(14,5,'WESTERN');
/*!40000 ALTER TABLE `subjects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `topics` (
  `topic_id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) NOT NULL,
  `topic_name` varchar(255) NOT NULL,
  PRIMARY KEY (`topic_id`),
  UNIQUE KEY `topic_id` (`topic_id`),
  KEY `subject_id` (`subject_id`),
  CONSTRAINT `topics_ibfk_1` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics`
--

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;
INSERT INTO `topics` VALUES (1,1,'NEWTONS THIRD LAW'),(2,1,'NEWTONS FIRST LAW'),(3,1,'NEWTONS SECOND LAW'),(4,1,'VELOCITY'),(5,1,'ACCELERATION'),(6,1,'GRAVITY'),(7,2,'ORGANIC CHEMISTRY'),(8,2,'CHEMITSRY NOMENCLATURE'),(9,2,'Atomic Structure'),(10,2,'Electrochemistry'),(11,2,'Units and Measurement'),(12,2,'Thermochemistry'),(13,2,'Periodic Table'),(14,2,'Equations and Stoichiometry'),(15,3,'DIFFERENTIATION'),(16,3,'INTEGRATION'),(17,3,'TRIGONOMETRY'),(18,3,'LAPLACE THEOREM'),(19,3,'Combinatorics'),(20,3,'Number theory'),(21,4,'Dynamical systems and differential equations'),(22,4,'Computation'),(23,4,'Information theory and signal processing'),(24,4,'Probability and statistics'),(25,5,'Kinematics'),(26,5,'Fluid Mechanics'),(27,5,'Electricity and Magnetism'),(28,5,'Oscillations and Waves'),(29,5,'Atomic, Nuclear, and Particle Physics'),(30,5,'THEORY OF GRAVITY'),(31,6,'Balance Sheet Reconciliations.'),(32,6,'Bank wires, receiving'),(33,6,'Chart of Accounts'),(34,5,'Correcting journals without index numbers'),(35,6,'Endowment'),(36,7,'dividend policy decisions'),(37,7,'dividend policy decisions'),(38,7,'financing decisions'),(39,7,'initial public offerings'),(40,8,'Managing technology & innovation'),(41,8,'Resources management & sustainable development'),(42,8,'Social entrepreneurship'),(43,8,'Corporate responsibility, ethics & accountability'),(44,8,'Accounting & finance'),(45,8,'Industrial management'),(46,9,'GEO SPATIAL SCIENCE'),(47,10,'Biological oceanography'),(48,10,'Physical oceanography'),(49,10,'Chemical oceanography');
/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutor_role`
--

DROP TABLE IF EXISTS `tutor_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tutor_role` (
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `tutor_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor_role`
--

LOCK TABLES `tutor_role` WRITE;
/*!40000 ALTER TABLE `tutor_role` DISABLE KEYS */;
INSERT INTO `tutor_role` VALUES (6),(7),(8),(9),(10);
/*!40000 ALTER TABLE `tutor_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutor_schedule`
--

DROP TABLE IF EXISTS `tutor_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tutor_schedule` (
  `schedule_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `class_size` int(11) NOT NULL,
  PRIMARY KEY (`schedule_id`),
  UNIQUE KEY `schedule_id` (`schedule_id`),
  KEY `user_id` (`user_id`),
  KEY `topic_id` (`topic_id`),
  CONSTRAINT `tutor_schedule_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`topic_id`),
  CONSTRAINT `tutor_schedule_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `tutor_role` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor_schedule`
--

LOCK TABLES `tutor_schedule` WRITE;
/*!40000 ALTER TABLE `tutor_schedule` DISABLE KEYS */;
INSERT INTO `tutor_schedule` VALUES (33,6,1,5),(34,6,2,5),(35,6,3,5),(36,6,4,5),(37,6,5,5),(38,7,6,5),(39,7,7,5),(40,7,8,5),(41,7,9,5),(42,7,10,5),(43,7,11,5),(44,7,12,5),(45,8,14,5),(46,8,15,5),(47,8,16,5),(48,8,17,5),(49,8,18,5),(50,9,25,5),(51,9,26,5),(52,9,27,5),(53,9,28,5),(54,9,29,5),(55,10,31,5),(56,10,23,5),(57,10,33,5),(58,10,34,5),(59,10,25,5);
/*!40000 ALTER TABLE `tutor_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `user_type` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'S'),(2,'S'),(3,'S'),(4,'S'),(5,'S'),(6,'T'),(7,'T'),(8,'T'),(9,'T'),(10,'T');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `email_id` varchar(255) NOT NULL,
  `password` varchar(15) DEFAULT NULL,
  `address_line1` varchar(255) NOT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `zip_code` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `email_unique` (`email_id`),
  KEY `zipCode` (`zip_code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Javed','M','Habib','javed@uncc.edu',NULL,'431 Barton Creek Drive','APT C','Charlotte','North Carolina','USA','28262'),(2,'David','M','Tickler','david@uncc.edu',NULL,'420 Barton Creek Drive','APT C','Charlotte','North Carolina','USA','28262'),(3,'Ramesh','Kumar','Palagati','ramesh@uncc.edu',NULL,'460 Barton Creek Drive','APT F','Charlotte','North Carolina','USA','28263'),(4,'Sandeep','Kumar','Pereddy','sreddy@uncc.edu',NULL,'440 Barton Creek Drive','APT B','Charlotte','North Carolina','USA','28262'),(5,'Zeeshan','Kumar','Kalra','zeeshan@uncc.edu',NULL,'436 Barton Creek Drive','APT B','Charlotte','North Carolina','USA','28262'),(6,'Rubina','Devi','Prasad','Rubina@uncc.edu',NULL,'437 Barton Creek Drive','APT B','Charlotte','North Carolina','USA','28262'),(7,'Shilpa','Kumari','Yadav','rratra@uncc.edu',NULL,'437 Barton Creek Drive','APT B','Charlotte','North Carolina','USA','28262'),(8,'JayShree','M','Reddy','jayshree@uncc.edu',NULL,'438 Barton Creek Drive','APT B','Charlotte','North Carolina','USA','28262'),(9,'Himanshu','J','Yadav','himanshu@uncc.edu',NULL,'439 University Terrace Creek Drive','APT B','Charlotte','North Carolina','USA','28262'),(10,'Hemant','Kumar','Yadav','hemant@uncc.edu',NULL,'436 Barton Creek Drive','APT B','Charlotte','North Carolina','USA','28262');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'project_schema'
--

--
-- Dumping routines for database 'project_schema'
--
/*!50003 DROP PROCEDURE IF EXISTS `REGISTER_TUTOR_SERVICE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `REGISTER_TUTOR_SERVICE`(IN userId INT, IN topicId INT, IN classSize INT, IN weekDay VARCHAR(255), IN fromDate VARCHAR(255), 
          IN toDate VARCHAR(255), IN serviceType VARCHAR(255))
begin 
DECLARE scheduleId int;
DECLARE tutorAvailabilityId int;
insert into tutor_schedule (user_id,topic_id,class_size)VALUES(userId,topicId, classSize);
select schedule_id  into scheduleId from tutor_schedule where user_id = userId and topic_id = topicId and class_size = classSize order by schedule_id desc limit 1;
insert into availability_timigs(schedule_id,service_type_id,week_day,avail_time_from,avail_time_to)values(scheduleId,serviceType,weekDay,fromDate,toDate);
select tutor_availability_id into tutorAvailabilityId from availability_timigs
where schedule_id = scheduleId 
   and service_type_id = serviceType and week_day = weekDay and avail_time_from = fromDate and avail_time_to = toDate;
insert into services_available(user_id,topic_id,tutor_availability_id)values(userId, topicId, tutorAvailabilityId);
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `REGISTER_USER` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `REGISTER_USER`(IN firstname VARCHAR(255), IN lastName VARCHAR(255), IN email_id VARCHAR(255),
IN user_password VARCHAR(255), IN addressLine1 VARCHAR(255), IN addressLine2 VARCHAR(255), IN city VARCHAR(255), 
IN state VARCHAR(255), IN  country VARCHAR(255), IN zipCode VARCHAR(255), IN userType VARCHAR(255), OUT result int)
BEGIN
	Declare user_ids int default -1;
	insert into users (first_name, last_name,email_id,user_password,address_line1,address_line2,city,state,country,zip_code) 
    values (firstname, lastName, email_id, user_password, addressLine1, addressLine2, city, state, country, zipCode);
    select u.user_id  into @user_ids from users u where u.email_id = email_id limit 1;
	if userType = "S" then
		insert into student_role (user_id) values(@user_ids);
    elseif userType = "T" then
		insert into tutor_role (user_id) values(@user_ids);
    elseif userType = "B" then
		insert into student_role (user_id) values(@user_ids);
        insert into tutor_role (user_id) values(@user_ids);
    end if;
    
    SET result = @user_ids;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `gettutorservicedetails`
--

/*!50001 DROP VIEW IF EXISTS `gettutorservicedetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `gettutorservicedetails` AS select `t`.`topic_name` AS `topic_name`,`s`.`subject_name` AS `subject_name`,`c`.`category_type` AS `category_type`,`at`.`avail_time_from` AS `avail_time_from`,`at`.`avail_time_to` AS `avail_time_to`,`st`.`service_type` AS `service_type`,`ts`.`class_size` AS `class_size`,`at`.`tutor_availability_id` AS `tutor_availability_id`,`sa`.`service_status` AS `service_status`,`sa`.`service_id` AS `service_id`,`ts`.`user_id` AS `user_id` from ((((((`tutor_schedule` `ts` join `topics` `t` on((`t`.`topic_id` = `ts`.`topic_id`))) join `subjects` `s` on((`s`.`subject_id` = `t`.`subject_id`))) join `category` `c` on((`c`.`category_id` = `s`.`category_id`))) join `availability_timigs` `at` on((`at`.`schedule_id` = `ts`.`schedule_id`))) join `services_types` `st` on((`st`.`service_type_id` = `at`.`service_type_id`))) join `services_available` `sa` on(((`sa`.`user_id` = `ts`.`user_id`) and (`at`.`tutor_availability_id` = `sa`.`tutor_availability_id`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `searchservices`
--

/*!50001 DROP VIEW IF EXISTS `searchservices`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `searchservices` AS select `usr`.`first_name` AS `first_name`,`usr`.`last_name` AS `last_name`,`sb`.`subject_name` AS `subject_name`,`sb`.`subject_id` AS `subject_id`,`tp`.`topic_id` AS `topic_id`,`tp`.`topic_name` AS `topic_name`,`sa`.`tutor_availability_id` AS `tutor_availability_id`,`usr`.`zip_code` AS `zip_code`,`sa`.`service_id` AS `service_id`,`sa`.`service_status` AS `service_status` from (((((`services_available` `sa` join `users` `usr` on((`sa`.`user_id` = `usr`.`user_id`))) join `tutor_role` `ur` on((`ur`.`user_id` = `usr`.`user_id`))) join `tutor_schedule` `ts` on((`ts`.`user_id` = `usr`.`user_id`))) join `topics` `tp` on((`tp`.`topic_id` = `ts`.`topic_id`))) join `subjects` `sb` on((`sb`.`subject_id` = `tp`.`subject_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-25 18:38:12
