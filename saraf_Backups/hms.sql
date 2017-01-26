/*
SQLyog Enterprise - MySQL GUI v7.02 
MySQL - 5.0.67-community-nt : Database - hms
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`hms` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `hms`;

/*Table structure for table `sign_up` */

DROP TABLE IF EXISTS `sign_up`;

CREATE TABLE `sign_up` (
  `name` varchar(200) default NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) default NULL,
  `mobile_no` varchar(15) default NULL,
  `role` varchar(10) default NULL,
  `token` tinyint(1) default NULL,
  `active` tinyint(1) default NULL,
  PRIMARY KEY  (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `sign_up` */

insert  into `sign_up`(`name`,`email`,`password`,`mobile_no`,`role`,`token`,`active`) values ('admin','admin@gmail.com','1','8764155254','Admin',1,1),('hospital','hospital@gmail.com','1','8764155254','Hospital',NULL,1),('medical','medical@gmail.com','1','8764155254','Medical',NULL,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
