CREATE TABLE `USER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(255) NOT NULL COMMENT 'User name for login',
  `PASSWORD` varchar(500) NOT NULL COMMENT 'Password for login',
  `ACTIVE` bit(1) DEFAULT b'1',
  `DEPOSIT` decimal(13,2) DEFAULT '0.00' COMMENT 'Deposit amount',
  `ROLE` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USER_USERNAME_uindex` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='Stores users'

CREATE TABLE `PRODUCT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AMOUNT_AVAILABLE` int(11) NOT NULL DEFAULT '0' COMMENT 'Amount of product available in the vending machine',
  `NAME` varchar(255) NOT NULL COMMENT 'Name of the product',
  `ACTIVE` bit(1) DEFAULT b'1',
  `COST` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT 'Cost of the product',
  `SELLER_ID` int(11) NOT NULL COMMENT 'The ID of the seller - points to user table',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PRODUCT_NAME_uindex` (`NAME`),
  KEY `PRODUCT_SELLER_ID_index` (`SELLER_ID`),
  CONSTRAINT `PRODUCT_SELLER_ID_fk` FOREIGN KEY (`SELLER_ID`) REFERENCES `USER` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='Stores products'