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
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Assessment`
--

LOCK TABLES `Assessment` WRITE;
/*!40000 ALTER TABLE `Assessment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Assessment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AssessmentQuestionSection`
--

DROP TABLE IF EXISTS `AssessmentQuestionSection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AssessmentQuestionSection` (
  `Id` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AssessmentQuestionSection`
--

LOCK TABLES `AssessmentQuestionSection` WRITE;
/*!40000 ALTER TABLE `AssessmentQuestionSection` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AssessmentSection`
--

LOCK TABLES `AssessmentSection` WRITE;
/*!40000 ALTER TABLE `AssessmentSection` DISABLE KEYS */;
/*!40000 ALTER TABLE `AssessmentSection` ENABLE KEYS */;
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
  CONSTRAINT `MultipleChoiceQuestionId` FOREIGN KEY (`MultipleChoiceQuestionId`) REFERENCES `MultipleChoiceQuestionType` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MultipleChoiceQuestionOption`
--

LOCK TABLES `MultipleChoiceQuestionOption` WRITE;
/*!40000 ALTER TABLE `MultipleChoiceQuestionOption` DISABLE KEYS */;
/*!40000 ALTER TABLE `MultipleChoiceQuestionOption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MultipleChoiceQuestionType`
--

DROP TABLE IF EXISTS `MultipleChoiceQuestionType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MultipleChoiceQuestionType` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Text` longtext NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MultipleChoiceQuestionType`
--

LOCK TABLES `MultipleChoiceQuestionType` WRITE;
/*!40000 ALTER TABLE `MultipleChoiceQuestionType` DISABLE KEYS */;
/*!40000 ALTER TABLE `MultipleChoiceQuestionType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Question`
--

DROP TABLE IF EXISTS `Question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `QuestionType` bigint(20) NOT NULL,
  `CreationDate` datetime NOT NULL,
  `UpdationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `QuestionType` (`QuestionType`),
  CONSTRAINT `QuestionType` FOREIGN KEY (`QuestionType`) REFERENCES `MultipleChoiceQuestionType` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Question`
--

LOCK TABLES `Question` WRITE;
/*!40000 ALTER TABLE `Question` DISABLE KEYS */;
/*!40000 ALTER TABLE `Question` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-25 17:06:53
