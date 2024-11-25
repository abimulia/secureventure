/**
 * 
 */
package com.abimulia.secureventure.user.query;

/**
 * 
 */
public class SqlQuery {
	
	public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, last_name, email, password) VALUES (:firstName,:lastName,:email,:password)";
	public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
	public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId,:url)";
	public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :roleName";
	public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO UserRoles (user_id,role_id) VALUES (:userId,:roleId)";
}
