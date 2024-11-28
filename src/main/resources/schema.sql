/*
 * --- General Rules ---
 * Use underscore_names instead of camelCase
 * Table names should be plural
 * Spell out id fields (item_id instead of id)
 * Don't use ambiguous column names
 * Name foreign key columns the same as the columns they refer to
 * Use caps for all SQL queries
 */

CREATE SCHEMA IF NOT EXISTS secvent;

USE secvent;

DROP TABLE IF EXISTS UserEvents;
DROP TABLE IF EXISTS UserRoles;
DROP TABLE IF EXISTS AccountVerifications;
DROP TABLE IF EXISTS TwoFAVerifications;
DROP TABLE IF EXISTS ResetPasswordVerifications;
DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
	user_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
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
);

DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles (
	role_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'Role ID',
	name varchar(50) NOT NULL COMMENT 'Role Name',
	permissions varchar(255) NOT NULL COMMENT "Role Permissions",
	CONSTRAINT Roles_UNIQUE UNIQUE KEY (name)
);

CREATE TABLE UserRoles (
	userrole_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	user_id BIGINT UNSIGNED NOT NULL,
	role_id BIGINT UNSIGNED NOT NULL,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (role_id) REFERENCES Roles(role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT UserRoles_User_UNIQUE UNIQUE KEY (user_id)
);

DROP TABLE IF EXISTS Events;

CREATE TABLE Events (
	event_id BIGINT UNSIGNED  AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'Event ID',
	type        VARCHAR(255) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILURE', 'LOGIN_ATTEMPT_SUCCESS', 'PROFILE_UPDATE', 'PROFILE_PICTURE_UPDATE', 'ROLE_UPDATE', 'ACCOUNT_SETTINGS_UPDATE', 'PASSWORD_UPDATE', 'MFA_UPDATE')),
	description varchar(255) NOT NULL COMMENT "Description",
	CONSTRAINT Events_UNIQUE UNIQUE KEY (type)
);

CREATE TABLE UserEvents (
	userevent_id BIGINT UNSIGNED  AUTO_INCREMENT NOT NULL PRIMARY KEY,
	user_id BIGINT UNSIGNED NOT NULL,
	event_id BIGINT UNSIGNED NOT NULL,
	device varchar(100) DEFAULT NULL,
	ip_address varchar(100) DEFAULT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE RESTRICT ON UPDATE CASCADE
);



CREATE TABLE AccountVerifications (
	av_id BIGINT UNSIGNED  AUTO_INCREMENT NOT NULL PRIMARY KEY,
	user_id BIGINT UNSIGNED NOT NULL,
	url varchar(255) NOT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT AccountVerifications_User_UNIQUE UNIQUE KEY (user_id),
	CONSTRAINT AccountVerifications_Url_UNIQUE UNIQUE KEY (url)
);



CREATE TABLE ResetPasswordVerifications (
	rpv_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	user_id BIGINT UNSIGNED NOT NULL,
	url varchar(255) NOT NULL,
	expiration_date DATETIME NOT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT ResetPasswordVerifications_User_UNIQUE UNIQUE KEY (user_id),
	CONSTRAINT ResetPasswordVerifications_Url_UNIQUE UNIQUE KEY (url)
);



CREATE TABLE TwoFAVerifications (
	tfav_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	user_id BIGINT UNSIGNED NOT NULL,
	code varchar(10) NOT NULL,
	expiration_date DATETIME NOT NULL,
	created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT TwoFAVerifications_User_UNIQUE UNIQUE KEY (user_id),
	CONSTRAINT TwoFAVerifications_Code_UNIQUE UNIQUE KEY (code)
);



