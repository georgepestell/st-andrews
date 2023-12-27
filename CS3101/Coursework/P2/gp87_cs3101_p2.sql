-- MariaDB dump 10.19  Distrib 10.5.18-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: cs3101_p2
-- ------------------------------------------------------
-- Server version	10.5.18-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dietary_requirement`
--

DROP TABLE IF EXISTS `dietary_requirement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dietary_requirement` (
  `short_name` varchar(20) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`short_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dietary_requirement`
--

LOCK TABLES `dietary_requirement` WRITE;
/*!40000 ALTER TABLE `dietary_requirement` DISABLE KEYS */;
INSERT INTO `dietary_requirement` VALUES ('Gluten-free','No gluten, e.g. wheat products.'),('Halal','Alcohol, some meats and a few other things to be avoided. Check https://thehalallife.co.uk/halal-check/'),('Vegan','No animal products of any kind, except maybe honey (check!).'),('Vegetarian','No meat or fish.');
/*!40000 ALTER TABLE `dietary_requirement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dinner_table`
--

DROP TABLE IF EXISTS `dinner_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dinner_table` (
  `table_no` tinyint(3) unsigned NOT NULL,
  `capacity` int(10) unsigned NOT NULL DEFAULT 1,
  PRIMARY KEY (`table_no`),
  CONSTRAINT `dinner_table_CHECK_positive_capacity` CHECK (`capacity` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinner_table`
--

LOCK TABLES `dinner_table` WRITE;
/*!40000 ALTER TABLE `dinner_table` DISABLE KEYS */;
INSERT INTO `dinner_table` VALUES (1,11),(2,8),(3,10),(4,10),(5,10),(6,4),(7,8),(8,8),(9,6),(10,8);
/*!40000 ALTER TABLE `dinner_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guest_diet`
--

DROP TABLE IF EXISTS `guest_diet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guest_diet` (
  `person_id` int(10) unsigned NOT NULL,
  `dietary_requirement_name` varchar(20) NOT NULL,
  PRIMARY KEY (`person_id`,`dietary_requirement_name`),
  KEY `guest_diet_FK` (`dietary_requirement_name`),
  CONSTRAINT `guest_diet_FK` FOREIGN KEY (`dietary_requirement_name`) REFERENCES `dietary_requirement` (`short_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `guest_diet_person_FK` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest_diet`
--

LOCK TABLES `guest_diet` WRITE;
/*!40000 ALTER TABLE `guest_diet` DISABLE KEYS */;
INSERT INTO `guest_diet` VALUES (25,'Gluten-free'),(25,'Vegetarian'),(26,'Vegan'),(27,'Vegan'),(28,'Gluten-free'),(28,'Vegan'),(32,'Vegetarian'),(33,'Vegetarian'),(40,'Gluten-free'),(40,'Vegan'),(43,'Vegetarian'),(44,'Vegetarian'),(45,'Vegetarian'),(47,'Halal'),(48,'Gluten-free'),(48,'Halal'),(51,'Vegetarian'),(52,'Vegetarian'),(61,'Vegetarian'),(62,'Vegetarian'),(63,'Vegetarian'),(64,'Gluten-free'),(64,'Vegetarian'),(65,'Vegetarian'),(66,'Vegan'),(67,'Vegan'),(75,'Vegetarian'),(76,'Vegetarian');
/*!40000 ALTER TABLE `guest_diet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitation`
--

DROP TABLE IF EXISTS `invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitation` (
  `code` varchar(10) NOT NULL,
  `address` varchar(245) DEFAULT NULL,
  `date_sent` date DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation`
--

LOCK TABLES `invitation` WRITE;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
INSERT INTO `invitation` VALUES ('AWGHZ','2 Woodleigh Road, Coventry, CV4 8GT','2023-02-25'),('CAUMN','35 Northway, Leamington Spa, CV31 2BW','2023-02-24'),('CIXGN','3 Dunmere Cottages, Par, PL24 2PD','2023-02-24'),('HTJPT','42 Manor Road, Ducklington, OX29 7YA','2023-03-01'),('IXKJB','9 Heol Dolfain, Ynysforgan, SA6 6QF','2023-03-07'),('JHAOJ','86 St Bartholomews Road, Nottingham, NG3 3ED','2023-02-24'),('JQBZV','Charter House, 100 Broad Street, Birmingham, B15 1AE','2023-03-07'),('JUQOK','3 Marchmont Gardens, Richmond, TW10 6ET','2023-02-24'),('KTANU','117 Hamstead Road, Handsworth, B20 2BT','2023-03-01'),('KYMTY','8 Farriers Court, Coleford, GL16 8AB','2023-02-24'),('LHGFM','4 Victoria Road, Cemaes Bay, LL67 0HR','2023-03-12'),('LLBQC','21 Crescent Gardens, Swanley, BR8 7HE','2023-02-24'),('LTOQV','126 Jersey Road, Blaengwynfi, SA13 3TE','2023-02-24'),('LUXNR','12 Helyg Road, Penmaenmawr, LL34 6HE','2023-03-01'),('MCZFI','11 Cragie Walk, Gillingham, ME8 9JH','2023-02-24'),('MIDZS','Dormouse, Tithe Laithe, Hoyland, S74 9DQ','2023-02-25'),('MSNNS','20 Teal Close, Askam-In-Furness, LA16 7JF','2023-03-07'),('NNNND','5 Sunningdale Avenue, Brigg, DN20 8QD','2023-03-01'),('NQWUC','6 The Causeway, Needham Market, IP6 8BD','2023-03-01'),('QGCJI','Glebe House, The Street, Itteringham, NR11 7AX','2023-02-24'),('RGWBV','Orchard Cottage, Ingram Lane, Grassthorpe, NG23 6RA','2023-03-01'),('RVQLU','215 Southcote Lane, Reading, RG30 3AY','2023-02-24'),('SADDT','40 Honey Lane, Buntingford, SG9 9BQ','2023-03-01'),('UAZEX','45 Harpes Road, Oxford, OX2 7QJ','2023-02-24'),('UBIHF','1 Red Gables, Puddington Village, Puddington, CH64 5ST','2023-02-24'),('ULYDN','11 Rutherglen Road, Sunderland, SR5 5LW','2023-02-24'),('UTZAF','11 Mayfield, Ivybridge, PL21 9UE','2023-03-03'),('VFAIW','Carreg Grwca, Glandwr, SA34 0UD','2023-02-25'),('VZLAR','11 Denton View, Blaydon-On-Tyne, NE21 4DZ','2023-03-02'),('ZTPAA','6 Almsgate, Compton, GU3 1JG','2023-02-25'),('ZYBXM','79 Hayling Rise, Worthing, BN13 3AG','2023-03-12');
/*!40000 ALTER TABLE `invitation` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017  /*!50003 TRIGGER insertCheckDateSentInPast
BEFORE INSERT
ON invitation FOR EACH ROW
BEGIN
	if (CURRENT_DATE() - NEW.date_sent) < 0 THEN
		signal sqlstate '23513' set message_text = "date_sent cannot be in the future";
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017  /*!50003 TRIGGER updateCheckDateSentInPast
BEFORE UPDATE
ON invitation FOR EACH ROW
BEGIN
	if (CURRENT_DATE() - NEW.date_sent) < 0 THEN
		signal sqlstate '23513' set message_text = "date_sent cannot be in the future";
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `organiser`
--

DROP TABLE IF EXISTS `organiser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organiser` (
  `password` varchar(100) NOT NULL,
  `person_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `organiser_person_FK` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organiser`
--

LOCK TABLES `organiser` WRITE;
/*!40000 ALTER TABLE `organiser` DISABLE KEYS */;
INSERT INTO `organiser` VALUES ('iheartpokemon1991',24),('abc123',27);
/*!40000 ALTER TABLE `organiser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `full_name` varchar(70) NOT NULL,
  `response` tinyint(1) DEFAULT NULL,
  `notes` varchar(100) DEFAULT NULL,
  `invitation_code` varchar(10) NOT NULL,
  `table_no` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `person_table_FK` (`table_no`),
  KEY `person_invitation_FK` (`invitation_code`),
  CONSTRAINT `person_invitation_FK` FOREIGN KEY (`invitation_code`) REFERENCES `invitation` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person_table_FK` FOREIGN KEY (`table_no`) REFERENCES `dinner_table` (`table_no`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (24,'Vicky Hernando',1,'','UAZEX',1),(25,'Solomiya Hernando',1,'','UAZEX',1),(26,'Davorka Hernando',1,'','UAZEX',1),(27,'Driskoll Devine',NULL,'','RVQLU',1),(28,'Sophie Devine',NULL,'','RVQLU',1),(29,'Anas Devine',NULL,'','RVQLU',1),(30,'Theo Hester',NULL,'','MCZFI',1),(31,'Morgan Martin',NULL,'Best man','KYMTY',1),(32,'Jody Sims',1,'Giving speech','LTOQV',1),(33,'Islay Sims',0,'Giving speech','LTOQV',1),(34,'Ashlyn Sims',0,'','JHAOJ',2),(35,'Ryan Sims',1,'Thank Ryan for the voucher he sent us.','LTOQV',2),(36,'Chung Mercado',NULL,'','ULYDN',2),(37,'Carmen Turner',NULL,'','QGCJI',2),(38,'Theresa Butler',1,'','LLBQC',2),(39,'Edmund Kaufman',1,'','UBIHF',2),(40,'Mason Gill',NULL,'','JUQOK',3),(41,'Fletcher Gill',1,'Doesn’t talk to Keira any more: keep them apart!','JUQOK',3),(42,'Tameka Gill',0,'Has guide dog called Amos, a very good boy.','CIXGN',3),(43,'Elvin Nielsen',1,'','CAUMN',3),(44,'Lyssa Nielsen',1,'','CAUMN',3),(45,'Keira Arnold',NULL,'','CAUMN',3),(46,'Benedict Arnold',NULL,'','CAUMN',3),(47,'Andrea Cardenas',NULL,'','MIDZS',4),(48,'Stephen Cardenas',NULL,'','MIDZS',4),(49,'Chris Cardenas',NULL,'','VFAIW',4),(50,'Margaret Garza',NULL,'','ZTPAA',4),(51,'Joann Alvarado',NULL,'','AWGHZ',4),(52,'Deana Alvarado',0,'','AWGHZ',4),(53,'Raymond Cox',0,'','SADDT',5),(54,'Don Cox',1,'','SADDT',5),(55,'Jimmie Frye',1,'Might try to play accordion: avoid this at all costs.','RGWBV',5),(56,'Lacey Frey',1,'','HTJPT',5),(57,'Priscilla Todd',NULL,'Wheelchair access needed','HTJPT',5),(58,'Yesenia Hall',NULL,'','HTJPT',5),(59,'Angelina Hall',1,'','HTJPT',5),(60,'Fran Spears',1,'','NQWUC',6),(61,'Lorraine Hahn',NULL,'','KTANU',6),(62,'Sherri Evans',1,'','NNNND',6),(63,'Kathryn Bates',1,'','NNNND',6),(64,'Mitchel Wyatt',1,'','LUXNR',7),(65,'Aaron Wyatt',1,'','LUXNR',7),(66,'Tara Wyatt',0,'','LUXNR',7),(67,'Dwayne Cross',NULL,'','VZLAR',8),(68,'Jackson Atkins',1,'','UTZAF',8),(69,'Ken Huff',1,'','VZLAR',8),(70,'Kellie Huff',NULL,'','VZLAR',9),(71,'Jeanine Medina',NULL,'','IXKJB',9),(72,'Tracie Palmer',1,'','JQBZV',9),(73,'Hollis Calhoun',1,'','MSNNS',9),(74,'Sonya Cohen',NULL,'','MSNNS',9),(75,'Clifton Newman',NULL,'Wheelchair access needed','ZYBXM',10),(76,'Katherine Fowler',0,'','LHGFM',10);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017  /*!50003 TRIGGER insertCheckTableCapacity
BEFORE INSERT
ON person FOR EACH ROW
BEGIN
	
	DECLARE table_capacity INT;
	DECLARE table_person_count INT;
	
	SELECT COUNT(*) FROM person p WHERE p.table_no = NEW.table_no INTO table_person_count;
	SELECT capacity FROM dinner_table dt WHERE dt.table_no = NEW.table_no INTO table_capacity;
		
	IF (table_person_count >= table_capacity) THEN
		signal sqlstate '45000' set message_text = "Max table capacity reached";
	END IF;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017  /*!50003 TRIGGER updateCheckTableCapacity
BEFORE UPDATE
ON person FOR EACH ROW
BEGIN
	
	DECLARE table_capacity INT;
	DECLARE table_person_count INT;
	
	SELECT COUNT(*) FROM person p WHERE p.table_no = NEW.table_no AND p.id != NEW.id INTO table_person_count;
	SELECT capacity FROM dinner_table dt WHERE dt.table_no = NEW.table_no INTO table_capacity;
		
	IF (table_person_count >= table_capacity) THEN
		signal sqlstate '45000' set message_text = "Max table capacity reached";
	END IF;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sessions` (
  `sid` varchar(255) NOT NULL,
  `sess` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`sess`)),
  `expired` datetime NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `view_awaiting_response`
--

DROP TABLE IF EXISTS `view_awaiting_response`;
/*!50001 DROP VIEW IF EXISTS `view_awaiting_response`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `view_awaiting_response` AS SELECT
 1 AS `Full name`,
  1 AS `ID`,
  1 AS `Date of invitation` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_special_diets`
--

DROP TABLE IF EXISTS `view_special_diets`;
/*!50001 DROP VIEW IF EXISTS `view_special_diets`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `view_special_diets` AS SELECT
 1 AS `Table number`,
  1 AS `Full name`,
  1 AS `Dietary requirement(s).` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_standard_dinner`
--

DROP TABLE IF EXISTS `view_standard_dinner`;
/*!50001 DROP VIEW IF EXISTS `view_standard_dinner`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `view_standard_dinner` AS SELECT
 1 AS `Table number`,
  1 AS `Number of standard-diet guests` */;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'cs3101_p2'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_add_organiser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
DELIMITER ;;
CREATE  PROCEDURE `proc_add_organiser`(
IN full_name VARCHAR(50),
IN password VARCHAR(100)
)
BEGIN
	
	DECLARE person_id INT;

	SELECT id INTO person_id FROM person p WHERE p.full_name = full_name;

	IF person_id IS NULL THEN
		signal sqlstate '23513' set message_text = "Person does not exist in database";
	END IF;

	INSERT INTO organiser (person_id, password) VALUES (person_id, password);
	
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_add_person` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
DELIMITER ;;
CREATE  PROCEDURE `proc_add_person`(
IN full_name VARCHAR(50),
IN notes VARCHAR(100),
IN invitation_code VARCHAR(10),
IN table_no INT
)
BEGIN
	
	IF full_name IS NULL THEN
		signal sqlstate '23513' set message_text = "full_name cannot be NULL";
	END IF;
	
	IF invitation_code IS NULL THEN
		signal sqlstate '23513' set message_text = "invitation_code cannot be NULL";
	END IF;
	
	IF table_no IS NULL THEN
		signal sqlstate '23513' set message_text = "table_no cannot be NULL";
	END IF;

	INSERT IGNORE INTO dinner_table (table_no) VALUES (table_no);
	
	INSERT IGNORE INTO invitation (code) VALUES (invitation_code);
	
	INSERT INTO person (full_name, notes, invitation_code, table_no) VALUES (full_name, notes, invitation_code, table_no);

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `view_awaiting_response`
--

/*!50001 DROP VIEW IF EXISTS `view_awaiting_response`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013  SQL SECURITY DEFINER */
/*!50001 VIEW `view_awaiting_response` AS select `p`.`full_name` AS `Full name`,`p`.`id` AS `ID`,`i`.`date_sent` AS `Date of invitation` from (`person` `p` join `invitation` `i` on(`p`.`invitation_code` = `i`.`code`)) where `p`.`response` is null order by `i`.`date_sent`,`p`.`full_name`,`p`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_special_diets`
--

/*!50001 DROP VIEW IF EXISTS `view_special_diets`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013  SQL SECURITY DEFINER */
/*!50001 VIEW `view_special_diets` AS select `p`.`table_no` AS `Table number`,`p`.`full_name` AS `Full name`,group_concat(`gd`.`dietary_requirement_name` order by `gd`.`dietary_requirement_name` ASC separator ',') AS `Dietary requirement(s).` from (`person` `p` join `guest_diet` `gd` on(`p`.`id` = `gd`.`person_id`)) group by `p`.`id` order by `p`.`table_no`,`p`.`full_name` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_standard_dinner`
--

/*!50001 DROP VIEW IF EXISTS `view_standard_dinner`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013  SQL SECURITY DEFINER */
/*!50001 VIEW `view_standard_dinner` AS select `dt`.`table_no` AS `Table number`,count(`dt`.`table_no`) AS `Number of standard-diet guests` from (`dinner_table` `dt` join (select `p`.`id` AS `id`,`p`.`table_no` AS `table_no` from (`person` `p` left join `guest_diet` `gd` on(`p`.`id` = `gd`.`person_id`)) group by `p`.`id` having count(`gd`.`dietary_requirement_name`) = 0) `people` on(`people`.`table_no` = `dt`.`table_no`)) group by `dt`.`table_no` order by `dt`.`table_no` */;
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

-- Dump completed on 2023-04-05 16:46:37
