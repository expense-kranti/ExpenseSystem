-- MySQL dump 10.13  Distrib 5.7.18, for Linux (x86_64)
--
-- Host: localhost    Database: MySqlCSRDB
-- ------------------------------------------------------
-- Server version	5.7.18

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
-- Table structure for table `Assessment`
--

DROP TABLE IF EXISTS `Assessment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assessment` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(256) NOT NULL,
  `MaxScore` varchar(128) DEFAULT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  `IsActive` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Assessment`
--

LOCK TABLES `Assessment` WRITE;
/*!40000 ALTER TABLE `Assessment` DISABLE KEYS */;
INSERT INTO `Assessment` VALUES (1,'Assesment1','10','2017-09-26 12:15:28','2017-09-26 12:15:28',1);
/*!40000 ALTER TABLE `Assessment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AssessmentQuestionSection`
--

DROP TABLE IF EXISTS `AssessmentQuestionSection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AssessmentQuestionSection` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `SectionId` bigint(20) NOT NULL,
  `QuestionId` bigint(20) NOT NULL,
  `QuestionScore` varchar(128) NOT NULL,
  `OrderId` varchar(45) NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `SectionId` (`SectionId`),
  KEY `QuestionId` (`QuestionId`),
  CONSTRAINT `QuestionId` FOREIGN KEY (`QuestionId`) REFERENCES `Question` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SectionId` FOREIGN KEY (`SectionId`) REFERENCES `AssessmentSection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AssessmentQuestionSection`
--

LOCK TABLES `AssessmentQuestionSection` WRITE;
/*!40000 ALTER TABLE `AssessmentQuestionSection` DISABLE KEYS */;
INSERT INTO `AssessmentQuestionSection` VALUES (1,1,1,'2','1','2017-09-26 12:25:29','2017-09-26 12:25:29'),(2,1,2,'2','2','2017-09-26 12:25:29','2017-09-26 12:25:29'),(3,2,3,'3','1','2017-09-26 12:25:29','2017-09-26 12:25:29'),(4,3,4,'3','1','2017-09-26 12:25:29','2017-09-26 12:25:29');
/*!40000 ALTER TABLE `AssessmentQuestionSection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AssessmentSection`
--

DROP TABLE IF EXISTS `AssessmentSection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AssessmentSection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AssessmentId` bigint(20) NOT NULL,
  `SectionName` varchar(256) NOT NULL,
  `OrderId` bigint(20) NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`AssessmentId`),
  KEY `AssessmentId` (`AssessmentId`),
  CONSTRAINT `AssessmentId` FOREIGN KEY (`AssessmentId`) REFERENCES `Assessment` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AssessmentSection`
--

LOCK TABLES `AssessmentSection` WRITE;
/*!40000 ALTER TABLE `AssessmentSection` DISABLE KEYS */;
INSERT INTO `AssessmentSection` VALUES (1,1,'Section A',1,'2017-09-26 12:16:18','2017-09-26 12:16:18'),(2,1,'Section B',2,'2017-09-26 12:16:28','2017-09-26 12:16:28'),(3,1,'Section C',3,'2017-09-26 12:16:35','2017-09-26 12:16:35');
/*!40000 ALTER TABLE `AssessmentSection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MultipleChoiceQuestion`
--

DROP TABLE IF EXISTS `MultipleChoiceQuestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MultipleChoiceQuestion` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Text` longtext NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  `QuestionId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Question_Id` (`QuestionId`),
  CONSTRAINT `Question_Id` FOREIGN KEY (`QuestionId`) REFERENCES `Question` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MultipleChoiceQuestion`
--

LOCK TABLES `MultipleChoiceQuestion` WRITE;
/*!40000 ALTER TABLE `MultipleChoiceQuestion` DISABLE KEYS */;
INSERT INTO `MultipleChoiceQuestion` VALUES (1,'What is your gender?','2017-09-26 11:43:42','2017-09-26 11:43:42',1),(2,'What is your name?','2017-09-26 11:43:51','2017-09-26 11:43:51',2),(3,'What is your dob?','2017-09-26 11:43:56','2017-09-26 11:43:56',3),(4,'What is your test name?','2017-09-26 11:45:32','2017-09-26 11:45:32',4);
/*!40000 ALTER TABLE `MultipleChoiceQuestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MultipleChoiceQuestionOption`
--

DROP TABLE IF EXISTS `MultipleChoiceQuestionOption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MultipleChoiceQuestionOption` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `MultipleChoiceQuestionId` bigint(20) NOT NULL,
  `OrderId` bigint(20) NOT NULL,
  `Text` longtext NOT NULL,
  `IsCorrect` tinyint(1) NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `MultipleChoiceQuestionId` (`MultipleChoiceQuestionId`),
  CONSTRAINT `MultipleChoiceQuestionId` FOREIGN KEY (`MultipleChoiceQuestionId`) REFERENCES `MultipleChoiceQuestion` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MultipleChoiceQuestionOption`
--

LOCK TABLES `MultipleChoiceQuestionOption` WRITE;
/*!40000 ALTER TABLE `MultipleChoiceQuestionOption` DISABLE KEYS */;
INSERT INTO `MultipleChoiceQuestionOption` VALUES (1,1,1,'Male',1,'2017-09-26 12:07:14','2017-09-26 12:07:14'),(2,1,2,'Female',0,'2017-09-26 12:07:36','2017-09-26 12:07:36'),(3,1,3,'SheMale',0,'2017-09-26 12:07:50','2017-09-26 12:07:50'),(4,2,1,'Love',1,'2017-09-26 12:08:32','2017-09-26 12:08:32'),(5,2,2,'Shiva',0,'2017-09-26 12:08:49','2017-09-26 12:08:49'),(6,2,3,'Aman',0,'2017-09-26 12:09:01','2017-09-26 12:09:01'),(7,2,4,'Sanjeev',0,'2017-09-26 12:09:08','2017-09-26 12:09:08'),(8,3,1,'29091993',1,'2017-09-26 12:10:14','2017-09-26 12:10:14'),(9,3,2,'29091994',0,'2017-09-26 12:10:59','2017-09-26 12:10:59'),(10,3,3,'29091995',0,'2017-09-26 12:11:56','2017-09-26 12:11:56'),(11,3,4,'29091996',0,'2017-09-26 12:12:04','2017-09-26 12:12:04'),(12,4,1,'test1',1,'2017-09-26 12:13:11','2017-09-26 12:13:11'),(13,4,2,'test2',0,'2017-09-26 12:13:23','2017-09-26 12:13:23'),(14,4,3,'test3',0,'2017-09-26 12:13:34','2017-09-26 12:13:34'),(15,4,4,'test4',0,'2017-09-26 12:13:43','2017-09-26 12:13:43');
/*!40000 ALTER TABLE `MultipleChoiceQuestionOption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Question`
--

DROP TABLE IF EXISTS `Question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Type` bigint(20) NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `QuestionType` (`Type`),
  CONSTRAINT `QuestionType` FOREIGN KEY (`Type`) REFERENCES `QuestionType` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Question`
--

LOCK TABLES `Question` WRITE;
/*!40000 ALTER TABLE `Question` DISABLE KEYS */;
INSERT INTO `Question` VALUES (1,1,'2017-09-26 12:03:57','2017-09-26 12:03:57'),(2,1,'2017-09-26 12:04:03','2017-09-26 12:04:03'),(3,1,'2017-09-26 12:04:06','2017-09-26 12:04:06'),(4,1,'2017-09-26 12:04:10','2017-09-26 12:04:10');
/*!40000 ALTER TABLE `Question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QuestionType`
--

DROP TABLE IF EXISTS `QuestionType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QuestionType` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` longtext NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QuestionType`
--

LOCK TABLES `QuestionType` WRITE;
/*!40000 ALTER TABLE `QuestionType` DISABLE KEYS */;
INSERT INTO `QuestionType` VALUES (1,'MultipleChoice','2017-09-26 11:39:57','2017-09-26 11:39:57');
/*!40000 ALTER TABLE `QuestionType` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-28 14:05:29
