
CREATE USER 'chat'@'localhost' IDENTIFIED BY 'correcthorsebatterystaple';
GRANT ALL PRIVILEGES ON chat.* TO 'chat'@'localhost';

DROP DATABASE IF EXISTS chat;
CREATE DATABASE chat;

USE chat;

CREATE TABLE user_type (
	`id` INT NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `index_user_type_on_code` (`code`)
);

CREATE TABLE `user` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_type_id` INT NOT NULL,
	`username` VARCHAR(255) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `index_user_on_username` (`username`),
	CONSTRAINT `user_to_user_type` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`)
);

CREATE TABLE message_type (
	`id` INT NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `index_message_type_on_code` (`code`)
);

CREATE TABLE message (
	`id` INT NOT NULL AUTO_INCREMENT,
	`message_type_id` INT NOT NULL,
	`timestamp` DATETIME NOT NULL,
	`customer_user_id` INT NOT NULL,
	`customer_service_user_id` INT NOT NULL,
	`message_body` VARCHAR(4095) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `message_to_customer_user` FOREIGN KEY (`customer_user_id`) REFERENCES `user` (`id`),
	CONSTRAINT `message_to_customer_service_user` FOREIGN KEY (`customer_service_user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE message_meta (
	`id` INT NOT NULL AUTO_INCREMENT,
	`message_id` INT NOT NULL,
	`meta_key` VARCHAR(255) NOT NULL,
	`value` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `message_meta_to_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`)
);

-- schema complete, now populate reference data
INSERT INTO user_type(code) VALUES('CUSTOMER');
INSERT INTO user_type(code) VALUES('CUSTOMER_SERVICE');

INSERT INTO message_type(code) VALUES('TEXT');
INSERT INTO message_type(code) VALUES('IMAGE_LINK');
INSERT INTO message_type(code) VALUES('VIDEO_LINK');
