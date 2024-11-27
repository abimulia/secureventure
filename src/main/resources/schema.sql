CREATE SCHEMA IF NOT EXISTS secvent;

SET NAMES 'UTF8MB4';

USE secvent;

DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
	user_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY,
	first_name varchar(50) NOT NULL COMMENT 'First Name',
	last_name varchar(50) NOT NULL COMMENT 'Last Name',
	email varchar(100) NOT NULL COMMENT 'Email',
	password varchar(255) DEFAULT NULL COMMENT 'Password',
	address varchar(255) DEFAULT NULL COMMENT 'Password',
	phone varchar(30) DEFAULT NULL COMMENT "Phone",
	title varchar(50) DEFAULT NULL COMMENT "Title",
	bio varchar(255) DEFAULT NULL COMMENT "Bio",
	enabled BOOLEAN DEFAULT FALSE,
	non_locked BOOLEAN DEFAULT TRUE,
	using_mfa BOOLEAN DEFAULT FALSE,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	update_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	image_url varchar(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png' COMMENT "Image URL",
	CONSTRAINT Users_UNIQUE UNIQUE KEY (email)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='Users table';

DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles (
	role_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY COMMENT 'Role ID',
	name varchar(50) NOT NULL COMMENT 'Role Name',
	permissions varchar(255) NOT NULL COMMENT "Role Permissions",
	CONSTRAINT Roles_UNIQUE UNIQUE KEY (name)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS UserRoles;

CREATE TABLE UserRoles (
	userrole_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY,
	user_id BIGINT unsigned NOT NULL,
	role_id BIGINT unsigned NOT NULL,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (role_id) REFERENCES Roles(role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT UserRoles_User_UNIQUE UNIQUE KEY (user_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS Events;

CREATE TABLE Events (
	event_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY COMMENT 'Event ID',
	type        VARCHAR(255) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILURE', 'LOGIN_ATTEMPT_SUCCESS', 'PROFILE_UPDATE', 'PROFILE_PICTURE_UPDATE', 'ROLE_UPDATE', 'ACCOUNT_SETTINGS_UPDATE', 'PASSWORD_UPDATE', 'MFA_UPDATE')),
	description varchar(255) NOT NULL COMMENT "Description",
	CONSTRAINT Events_UNIQUE UNIQUE KEY (type)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS UserEvents;

CREATE TABLE UserEvents (
	userevent_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY,
	user_id BIGINT unsigned NOT NULL,
	event_id BIGINT unsigned NOT NULL,
	device varchar(100) DEFAULT NULL,
	ip_address varchar(100) DEFAULT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE RESTRICT ON UPDATE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS AccountVerifications;

CREATE TABLE AccountVerifications (
	av_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY,
	user_id BIGINT unsigned NOT NULL,
	url varchar(255) NOT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT AccountVerifications_User_UNIQUE UNIQUE KEY (user_id),
	CONSTRAINT AccountVerifications_Url_UNIQUE UNIQUE KEY (url)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS ResetPasswordVerifications;

CREATE TABLE ResetPasswordVerifications (
	rpv_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY,
	user_id BIGINT unsigned NOT NULL,
	url varchar(255) NOT NULL,
	expiration_date DATETIME NOT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT ResetPasswordVerifications_User_UNIQUE UNIQUE KEY (user_id),
	CONSTRAINT ResetPasswordVerifications_Url_UNIQUE UNIQUE KEY (url)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS TwoFAVerifications;

CREATE TABLE TwoFAVerifications (
	tfav_id BIGINT unsigned auto_increment NOT NULL PRIMARY KEY,
	user_id BIGINT unsigned NOT NULL,
	code varchar(10) NOT NULL,
	expiration_date DATETIME NOT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT TwoFAVerifications_User_UNIQUE UNIQUE KEY (user_id),
	CONSTRAINT TwoFAVerifications_Code_UNIQUE UNIQUE KEY (code)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO Roles (name,permissions)
VALUES('ROLE_USER','READ:USER,READ:CUSTOMER'),
      ('ROLE_MANAGER','READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
      ('ROLE_ADMIN','READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
      ('ROLE_SYSADMIN','READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');

INSERT INTO Events(`type`, description)
VALUES ('LOGIN_ATTEMPT', 'You tried to log in'),
       ('LOGIN_ATTEMPT_FAILURE', 'You tried to log in and you failed'),
	   ('LOGIN_ATTEMPT_SUCCESS', 'You tried to log in and you succeeded'),
	   ('PROFILE_UPDATE', 'You updated your profile information'),
	   ('PROFILE_PICTURE_UPDATE', 'You updated your profile picture'),
	   ('ROLE_UPDATE', 'You updated your role and permissions'),
	   ('ACCOUNT_SETTINGS_UPDATE', 'You updated your account settings'),
	   ('MFA_UPDATE', 'You updated your MFA settings'),
	   ('PASSWORD_UPDATE', 'You updated your password'); 

