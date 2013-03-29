SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `ted2012` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `ted2012` ;

-- -----------------------------------------------------
-- Table `ted2012`.`jobs`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ted2012`.`jobs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(200) NULL DEFAULT NULL ,
  `startdate` DATETIME NOT NULL ,
  `enddate` DATETIME NOT NULL ,
  `state` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ted2012`.`users`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ted2012`.`users` (
  `username` VARCHAR(45) NOT NULL ,
  `password` VARCHAR(80) NOT NULL ,
  `name` VARCHAR(45) NULL DEFAULT NULL ,
  `surname` VARCHAR(45) NULL DEFAULT NULL ,
  `email` VARCHAR(45) NULL DEFAULT NULL ,
  `role` INT(11) NOT NULL ,
  PRIMARY KEY (`username`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ted2012`.`comments`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ted2012`.`comments` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `comment` VARCHAR(200) NOT NULL ,
  `job` INT(11) NOT NULL ,
  `writer` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `job` (`job` ASC) ,
  INDEX `writer` (`writer` ASC) ,
  INDEX `writer2` (`writer` ASC) ,
  CONSTRAINT `job1`
    FOREIGN KEY (`job` )
    REFERENCES `ted2012`.`jobs` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `writer2`
    FOREIGN KEY (`writer` )
    REFERENCES `ted2012`.`users` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ted2012`.`jobhasstaff`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ted2012`.`jobhasstaff` (
  `jobs` INT(11) NOT NULL ,
  `user` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`jobs`, `user`) ,
  INDEX `users` (`user` ASC) ,
  INDEX `jobs1` (`jobs` ASC) ,
  CONSTRAINT `jobs1`
    FOREIGN KEY (`jobs` )
    REFERENCES `ted2012`.`jobs` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `users1`
    FOREIGN KEY (`user` )
    REFERENCES `ted2012`.`users` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ted2012`.`projects`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ted2012`.`projects` (
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(200) NULL DEFAULT NULL ,
  `public` BIT(1) NOT NULL ,
  `manager` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`name`) ,
  INDEX `manager1` (`manager` ASC) ,
  CONSTRAINT `manager1`
    FOREIGN KEY (`manager` )
    REFERENCES `ted2012`.`users` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ted2012`.`projhasjobs`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ted2012`.`projhasjobs` (
  `project` VARCHAR(45) NOT NULL ,
  `job` INT(11) NOT NULL ,
  PRIMARY KEY (`project`, `job`) ,
  INDEX `jobs1` (`job` ASC) ,
  CONSTRAINT `jobs2`
    FOREIGN KEY (`job` )
    REFERENCES `ted2012`.`jobs` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `projects2`
    FOREIGN KEY (`project` )
    REFERENCES `ted2012`.`projects` (`name` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `ted2012`.`projhasstaff`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ted2012`.`projhasstaff` (
  `project` VARCHAR(45) NOT NULL ,
  `user` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`project`, `user`) ,
  INDEX `proj` (`project` ASC) ,
  INDEX `staf` (`user` ASC) ,
  CONSTRAINT `projects3`
    FOREIGN KEY (`project` )
    REFERENCES `ted2012`.`projects` (`name` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `users3`
    FOREIGN KEY (`user` )
    REFERENCES `ted2012`.`users` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `ted2012`.`users` (`username`, `password`, `name`, `surname`, `email`, `role`) VALUES ('ted2012', 'aovzxrqi24cbb150be287718a5a8c91e2fabef4f4bf25fab8cbf336be329fcdf15f6c8c2', 'adminName', 'adminSurname', 'admin@yahoo.com', 0);
