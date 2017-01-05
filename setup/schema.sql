
DROP DATABASE IF EXISTS chat;
CREATE DATABASE chat;

USE chat;

-- DROP TABLE IF EXISTS user_type;
CREATE TABLE user_type (
	`id` INT NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `index_user_type_on_code` (`code`)
);

-- DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_type_id` INT NOT NULL,
	`username` VARCHAR(255) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `index_user_on_username` (`username`),
	CONSTRAINT `user_to_user_type` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`)
);

-- DROP TABLE IF EXISTS message_type;
CREATE TABLE message_type (
	`id` INT NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `index_message_type_on_code` (`code`)
);

-- DROP TABLE IF EXISTS message;
CREATE TABLE message (
	`id` INT NOT NULL AUTO_INCREMENT,
	`message_type_id` INT NOT NULL,
	`customer_user_id` INT NOT NULL,
	`customer_service_user_id` INT NOT NULL,
	`message_body` VARCHAR(4095) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `message_to_customer_user` FOREIGN KEY (`customer_user_id`) REFERENCES `user` (`id`),
	CONSTRAINT `message_to_customer_service_user` FOREIGN KEY (`customer_service_user_id`) REFERENCES `user` (`id`)
);


-- schema complete, now populate reference data
INSERT INTO user_type(code) VALUES('CUSTOMER');
INSERT INTO user_type(code) VALUES('CUSTOMER_SERVICE');

INSERT INTO message_type(code) VALUES('TEXT');
INSERT INTO message_type(code) VALUES('IMAGE_LINK');
INSERT INTO message_type(code) VALUES('VIDEO_LINK');

-- TODO: message_meta table?
