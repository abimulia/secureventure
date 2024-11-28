/*
 * --- General Rules ---
 * Use underscore_names instead of camelCase
 * Table names should be plural
 * Spell out id fields (item_id instead of id)
 * Don't use ambiguous column names
 * Name foreign key columns the same as the columns they refer to
 * Use caps for all SQL queries
 */
USE secvent;

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
