/**
 * 
 */
package com.abimulia.secureventure.user.resource;

import static java.util.Map.of;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abimulia.secureventure.domain.HttpResponse;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;
import com.abimulia.secureventure.user.service.UserService;

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
	private final UserService userSerice;

	@PostMapping("/register")
	public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
		log.debug("saveUser()" + user);
		UserDTO userDTO = userSerice.createUser(user);
		return ResponseEntity.created(getUri())
				.body(HttpResponse.builder().timeStamps(LocalDateTime.now().toString()).message("User created")
						.status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).data(of("user", userDTO))
						.build());
	}

	private URI getUri() {
		return URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
	}

}
