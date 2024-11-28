/**
 * 
 */
package com.abimulia.secureventure.user.resource;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abimulia.secureventure.domain.HttpResponse;
import com.abimulia.secureventure.domain.UserPrincipal;
import com.abimulia.secureventure.exception.ApiException;
import com.abimulia.secureventure.form.LoginForm;
import com.abimulia.secureventure.form.SettingsForm;
import com.abimulia.secureventure.form.UpdateForm;
import com.abimulia.secureventure.form.UpdatePasswordForm;
import com.abimulia.secureventure.provider.TokenProvider;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;
import com.abimulia.secureventure.user.event.NewUserEvent;
import com.abimulia.secureventure.user.mapper.UserDTOMapper;
import com.abimulia.secureventure.user.service.EventService;
import com.abimulia.secureventure.user.service.RoleService;
import com.abimulia.secureventure.user.service.UserService;
import static com.abimulia.secureventure.utils.UserUtils.getAuthenticatedUser;
import static com.abimulia.secureventure.utils.UserUtils.getLoggedInUser;
import static com.abimulia.secureventure.utils.ExceptionUtils.processError;
import static com.abimulia.secureventure.user.enums.EventType.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 2:09:39 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@RestController
@Slf4j
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
	private static final String TOKEN_PREFIX = "Bearer ";
	private final UserService userService;
	private final RoleService roleService;
	private final EventService eventService;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final ApplicationEventPublisher publisher;

	@PostMapping("/register")
	public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
		log.debug("saveUser()" + user);
		UserDTO userDTO = userService.createUser(user);
		return ResponseEntity.created(getUri())
				.body(HttpResponse.builder().
						timeStamps(LocalDateTime.now().toString())
						.message("User created")
						.status(HttpStatus.CREATED)
						.statusCode(HttpStatus.CREATED.value())
						.data(of("user", userDTO))
						.build());
	}

	@PostMapping("/login")
	public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
		log.debug("login() " + loginForm.getEmail());
		UserDTO user = authenticate(loginForm.getEmail(), loginForm.getPassword());
		log.info("User: " + user);
		return user.isUsingMfa() ? sendVerificationCode(user) : sendResponse(user);
	}

	// method to get user by id
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserDTO> getByUserId(@PathVariable Long userId) {
		log.debug("getByUserId() userId: " + userId);
		UserDTO user = userService.getUserById(userId);
		return ResponseEntity.ok(user);

	}

	@GetMapping("/list")
	public ResponseEntity<HttpResponse> listUsers(Authentication authentication) {
		log.debug("listUsers() authentication" + authentication);
		return ResponseEntity.ok().body(HttpResponse.builder().timeStamps(now().toString())
				.data(of("users", userService.list())).message("Users Retrieved").build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpResponse> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok().body(HttpResponse.builder().timeStamps(now().toString())
				.data(of("message", "User Deleted")).message("User Deleted").status(OK).statusCode(OK.value()).build());
	}

	@GetMapping("/profile")
	public ResponseEntity<HttpResponse> profile(Authentication authentication) {
		log.debug("profile() authentication: " + authentication);
		UserDTO user = userService.getUserByEmail(getAuthenticatedUser(authentication).getEmail());
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamps(now().toString())
						.data(of("user", user, "events", eventService.getEventsByUserId(user.getId()), "roles",
								roleService.getRoleByUserId(user.getId())))
						.message("Profile Retrieved").status(OK).statusCode(OK.value()).build());
	}

	@PatchMapping("/update")
	public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UpdateForm user) {
		log.debug("updateUser() user: " + user);
		UserDTO updatedUser = userService.updateUserDetails(user);
		publisher.publishEvent(new NewUserEvent(updatedUser.getEmail(), PROFILE_UPDATE));
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamps(now().toString())
						.data(of("user", updatedUser, "events", eventService.getEventsByUserId(user.getId()), "roles",
								roleService.getRoleByUserId(user.getId())))
						.message("User updated").status(OK).statusCode(OK.value()).build());
	}

	// START - To reset password when user is not logged in
	@GetMapping("/verify/code/{email}/{code}")
	public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email,
			@PathVariable("code") String code) {
		UserDTO user = userService.verifyCode(email, code);
		publisher.publishEvent(new NewUserEvent(user.getEmail(), LOGIN_ATTEMPT_SUCCESS));
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamps(now().toString())
						.data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
								"refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
						.message("Login Success").status(OK).statusCode(OK.value()).build());
	}

	@GetMapping("/resetpassword/{email}")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {
		userService.resetPassword(email);
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamps(now().toString())
						.message("Email sent. Please check your email to reset your password.").status(OK)
						.statusCode(OK.value()).build());
	}

	@GetMapping("/verify/password/{key}")
	public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) {
		UserDTO user = userService.verifyPasswordKey(key);
		return ResponseEntity.ok().body(HttpResponse.builder().timeStamps(now().toString()).data(of("user", user))
				.message("Please enter a new password").status(OK).statusCode(OK.value()).build());
	}

	@PostMapping("/resetpassword/{key}/{password}/{confirmPassword}")
	public ResponseEntity<HttpResponse> resetPasswordWithKey(@PathVariable("key") String key,
			@PathVariable("password") String password, @PathVariable("confirmPassword") String confirmPassword) {
		userService.renewPassword(key, password, confirmPassword);
		return ResponseEntity.ok().body(HttpResponse.builder().timeStamps(now().toString())
				.message("Password reset successfully").status(OK).statusCode(OK.value()).build());
	}

	// END - To reset password when user is not logged in

	@PatchMapping("/update/password")
	public ResponseEntity<HttpResponse> updatePassword(Authentication authentication,
			@RequestBody @Valid UpdatePasswordForm form) {
		UserDTO userDTO = getAuthenticatedUser(authentication);
		userService.updatePassword(userDTO.getId(), form.getCurrentPassword(), form.getNewPassword(),
				form.getConfirmNewPassword());
		publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), PASSWORD_UPDATE));
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamps(now().toString())
						.data(of("user", userService.getUserById(userDTO.getId()), "events",
								eventService.getEventsByUserId(userDTO.getId()), "roles", roleService.getRoles()))
						.message("Password updated successfully").status(OK).statusCode(OK.value()).build());
	}

	@PatchMapping("/update/role/{roleName}")
	public ResponseEntity<HttpResponse> updateUserRole(Authentication authentication,
			@PathVariable("roleName") String roleName) {
		UserDTO userDTO = getAuthenticatedUser(authentication);
		userService.updateUserRole(userDTO.getId(), roleName);
		publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), ROLE_UPDATE));
		return ResponseEntity.ok()
				.body(HttpResponse.builder()
						.data(of("user", userService.getUserById(userDTO.getId()), "events",
								eventService.getEventsByUserId(userDTO.getId()), "roles", roleService.getRoles()))
						.timeStamps(now().toString()).message("Role updated successfully").status(OK)
						.statusCode(OK.value()).build());
	}

	@PatchMapping("/update/settings")
	public ResponseEntity<HttpResponse> updateAccountSettings(Authentication authentication,
			@RequestBody @Valid SettingsForm form) {
		UserDTO userDTO = getAuthenticatedUser(authentication);
		userService.updateAccountSettings(userDTO.getId(), form.getEnabled(), form.getNotLocked());
		publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), ACCOUNT_SETTINGS_UPDATE));
		return ResponseEntity.ok()
				.body(HttpResponse.builder()
						.data(of("user", userService.getUserById(userDTO.getId()), "events",
								eventService.getEventsByUserId(userDTO.getId()), "roles", roleService.getRoles()))
						.timeStamps(now().toString()).message("Account settings updated successfully").status(OK)
						.statusCode(OK.value()).build());
	}

	@PatchMapping("/togglemfa")
	public ResponseEntity<HttpResponse> toggleMfa(Authentication authentication) throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		UserDTO user = userService.toggleMfa(getAuthenticatedUser(authentication).getEmail());
		publisher.publishEvent(new NewUserEvent(user.getEmail(), MFA_UPDATE));
		return ResponseEntity.ok()
				.body(HttpResponse.builder()
						.data(of("user", user, "events", eventService.getEventsByUserId(user.getId()), "roles",
								roleService.getRoles()))
						.timeStamps(now().toString()).message("Multi-Factor Authentication updated").status(OK)
						.statusCode(OK.value()).build());
	}

	@PatchMapping("/update/image")
	public ResponseEntity<HttpResponse> updateProfileImage(Authentication authentication,
			@RequestParam("image") MultipartFile image) throws InterruptedException {
		UserDTO user = getAuthenticatedUser(authentication);
		userService.updateImage(user, image);
		publisher.publishEvent(new NewUserEvent(user.getEmail(), PROFILE_PICTURE_UPDATE));
		return ResponseEntity.ok()
				.body(HttpResponse.builder()
						.data(of("user", userService.getUserById(user.getId()), "events",
								eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles()))
						.timeStamps(now().toString()).message("Profile image updated").status(OK).statusCode(OK.value())
						.build());
	}

	@GetMapping(value = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
	public byte[] getProfileImage(@PathVariable("fileName") String fileName) throws Exception {
		return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
	}

	@GetMapping("/verify/account/{key}")
	public ResponseEntity<HttpResponse> verifyAccount(@PathVariable("key") String key) {
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamps(now().toString()).message(
						userService.verifyAccountKey(key).isEnabled() ? "Account already verified" : "Account verified")
						.status(OK).statusCode(OK.value()).build());
	}

	@GetMapping("/refresh/token")
	public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
		if (isHeaderAndTokenValid(request)) {
			String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
			UserDTO user = userService.getUserById(tokenProvider.getSubject(token, request));
			return ResponseEntity.ok()
					.body(HttpResponse.builder().timeStamps(now().toString())
							.data(of("user", user, "access_token",
									tokenProvider.createAccessToken(getUserPrincipal(user)), "refresh_token", token))
							.message("Token refreshed").status(OK).statusCode(OK.value()).build());
		} else {
			return ResponseEntity.badRequest()
					.body(HttpResponse.builder().timeStamps(now().toString()).reason("Refresh Token missing or invalid")
							.developerMessage("Refresh Token missing or invalid").status(BAD_REQUEST)
							.statusCode(BAD_REQUEST.value()).build());
		}
	}

	private boolean isHeaderAndTokenValid(HttpServletRequest request) {
		return request.getHeader(AUTHORIZATION) != null && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
				&& tokenProvider
						.isTokenValid(
								tokenProvider.getSubject(
										request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
								request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()));
	}

	@RequestMapping("/error")
	public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
		return ResponseEntity.badRequest().body(HttpResponse.builder().timeStamps(now().toString())
				.reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
				.status(BAD_REQUEST).statusCode(BAD_REQUEST.value()).build());
	}

	private UserDTO authenticate(String email, String password) {
		log.debug("-authenticate() email: " + email);
		try {
			if (userService.getUserByEmail(email) != null) {
				publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT));
			} else {
				throw new UsernameNotFoundException("User " + email + " not found.");
			}
			Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
			UserDTO loggedInUser = getLoggedInUser(authentication);
			if (!loggedInUser.isUsingMfa()) {
				publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT_SUCCESS));
			}
			log.info("User " + loggedInUser.getEmail() + "authenticated");
			return loggedInUser;
		} catch (Exception exception) {
			log.error("failed to authenticate, " + exception.getMessage());
//            publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT_FAILURE));
			processError(request, response, exception);
			throw new ApiException("Oops... failed to authenticate user " + email, exception);
		}
	}

	private URI getUri() {
		return URI.create(fromCurrentContextPath().path("/user/get/<userId>").toUriString());
	}

	private ResponseEntity<HttpResponse> sendResponse(UserDTO user) {
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamps(now().toString())
						.data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
								"refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
						.message("Login Success").status(OK).statusCode(OK.value()).build());
	}

	private UserPrincipal getUserPrincipal(UserDTO user) {
		return new UserPrincipal(UserDTOMapper.toUser(userService.getUserByEmail(user.getEmail())),
				roleService.getRoleByUserId(user.getId()));
	}

	private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
		userService.sendVerificationCode(user);
		return ResponseEntity.ok().body(HttpResponse.builder().timeStamps(now().toString()).data(of("user", user))
				.message("Verification Code Sent").status(OK).statusCode(OK.value()).build());
	}

}
