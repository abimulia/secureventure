/**
 * 
 */
package com.abimulia.secureventure.user.query;

/**
 * 
 */
public class SqlQuery {
	// User
	public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, last_name, email, password) VALUES (:firstName,:lastName,:email,:password)";
	public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
	public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId,:url)";
	public static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT * FROM Users WHERE email = :email";
	public static final String SELECT_ALL_USER = "SELECT * FROM users";
	public static final String DELETE_FROM_USERS_BY_USER_ID = "DELETE FROM users WHERE user_id = :userId";
    public static final String DELETE_VERIFICATION_CODE_BY_USER_ID = "DELETE FROM TwoFAVerifications WHERE user_id = :id";
    public static final String INSERT_VERIFICATION_CODE_QUERY = "INSERT INTO TwoFAVerifications (user_id, code, expiration_date) VALUES (:userId, :code, :expirationDate)";
    public static final String SELECT_USER_BY_USER_CODE_QUERY = "SELECT * FROM Users WHERE user_id = (SELECT user_id FROM TwoFAVerifications WHERE code = :code)";
    public static final String DELETE_CODE = "DELETE FROM TwoFAVerifications WHERE code = :code";
    public static final String SELECT_CODE_EXPIRATION_QUERY = "SELECT expiration_date < NOW() AS is_expired FROM TwoFAVerifications WHERE code = :code";
    public static final String DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY = "DELETE FROM ResetPasswordVerifications WHERE user_id = :userId";
    public static final String INSERT_PASSWORD_VERIFICATION_QUERY = "INSERT INTO ResetPasswordVerifications (user_id, url, expiration_date) VALUES (:userId, :url, :expirationDate)";
    public static final String SELECT_EXPIRATION_BY_URL = "SELECT expiration_date < NOW() AS is_expired FROM ResetPasswordVerifications WHERE url = :url";
    public static final String SELECT_USER_BY_PASSWORD_URL_QUERY = "SELECT * FROM Users WHERE user_id = (SELECT user_id FROM ResetPasswordVerifications WHERE url = :url)";
    public static final String UPDATE_USER_PASSWORD_BY_URL_QUERY = "UPDATE Users SET password = :password WHERE user_id = (SELECT user_id FROM ResetPasswordVerifications WHERE url = :url)";
    public static final String DELETE_VERIFICATION_BY_URL_QUERY = "DELETE FROM ResetPasswordVerifications WHERE url = :url";
    public static final String SELECT_USER_BY_ACCOUNT_URL_QUERY = "SELECT * FROM Users WHERE user_id = (SELECT user_id FROM AccountVerifications WHERE url = :url)";
    public static final String UPDATE_USER_ENABLED_QUERY = "UPDATE Users SET enabled = :enabled WHERE user_id = :id";
    public static final String UPDATE_USER_DETAILS_QUERY = "UPDATE Users SET first_name = :firstName, last_name = :lastName, email = :email, phone = :phone, address = :address, title = :title, bio = :bio WHERE user_id = :id";
    public static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE user_id = :id";
    public static final String UPDATE_USER_PASSWORD_BY_ID_QUERY = "UPDATE Users SET password = :password WHERE user_id = :userId";
    public static final String UPDATE_USER_SETTINGS_QUERY = "UPDATE Users SET enabled = :enabled, non_locked = :notLocked WHERE user_id = :userId";
    public static final String TOGGLE_USER_MFA_QUERY = "UPDATE Users SET using_mfa = :isUsingMfa WHERE email = :email";
    public static final String UPDATE_USER_IMAGE_QUERY = "UPDATE Users SET image_url = :imageUrl WHERE user_id = :id";
	// Role
	public static final String SELECT_ROLES_QUERY = "SELECT * FROM Roles ORDER BY id";
	public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO UserRoles (user_id,role_id) VALUES (:userId,:roleId)";
	public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :roleName";
	public static final String SELECT_ROLE_BY_ID_QUERY = "SELECT r.id, r.name, r.permission FROM Roles r JOIN UserRoles ur ON ur.role_id = r.id JOIN Users u ON u.user_id = ur.user_id WHERE u.user_id = :id";
	public static final String SELECT_ROLE_BY_EMAIL_QUERY = "SELECT r.id, r.name, r.permission FROM Roles r JOIN UserRoles ur ON ur.role_id = r.id JOIN Users u ON u.user_id = ur.user_id WHERE u.email = :email";
	public static final String UPDATE_USER_ROLE_QUERY = "UPDATE UserRoles SET role_id = :roleId WHERE user_id = :userId";
	// Event
	public static final String SELECT_EVENTS_BY_USER_ID_QUERY = "SELECT uev.user_event_id, uev.device, uev.ip_address, ev.type, ev.description, uev.created_date FROM Events ev JOIN UserEvents uev ON ev.event_id = uev.event_id JOIN Users u ON u.user_id = uev.user_id WHERE u.user_id = :id ORDER BY uev.created_date DESC LIMIT 10";
    public static final String INSERT_EVENT_BY_USER_EMAIL_QUERY = "INSERT INTO UserEvents (user_id, event_id, device, ip_address) VALUES ((SELECT user_id FROM Users WHERE email = :email), (SELECT event_id FROM Events WHERE type = :type), :device, :ipAddress)";
    public static final String INSERT_EVENT_BY_USER_ID_QUERY = "INSERT INTO UserEvents (user_id, event_id, device, ip_address) VALUES ((SELECT user_id FROM Users WHERE user_id = :id), (SELECT event_id FROM Events WHERE type = :type), :device, :ipAddress)";
	

}
