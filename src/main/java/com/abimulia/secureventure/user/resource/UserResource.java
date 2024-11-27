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
import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abimulia.secureventure.domain.HttpResponse;
import com.abimulia.secureventure.domain.UserPrincipal;
import com.abimulia.secureventure.exception.ApiException;
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
				.body(HttpResponse.builder().timeStamps(LocalDateTime.now().toString()).message("User created")
						.status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).data(of("user", userDTO))
						.build());
	}

	private UserDTO authenticate(String email, String password) {
        try {
            if(null != userService.getUserByEmail(email)) {
                publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT));
            }
            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
            UserDTO loggedInUser = getLoggedInUser(authentication);
            if(!loggedInUser.isUsingMfa()) {
                publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT_SUCCESS));
            }
            return loggedInUser;
        } catch (Exception exception) {
            publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT_FAILURE));
            processError(request, response, exception);
            throw new ApiException(exception.getMessage());
        }
    }

    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO user) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
                        , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private UserPrincipal getUserPrincipal(UserDTO user) {
        return new UserPrincipal(UserDTOMapper.toUser(userService.getUserByEmail(user.getEmail())), roleService.getRoleByUserId(user.getId()));
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", user))
                        .message("Verification Code Sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

}
