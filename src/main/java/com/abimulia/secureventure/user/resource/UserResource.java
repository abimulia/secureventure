/**
 * 
 */
package com.abimulia.secureventure.user.resource;

import java.awt.desktop.UserSessionEvent;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abimulia.secureventure.user.domain.HttpResponse;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;
import com.abimulia.secureventure.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.Map.of;
/**
 * 
 */
@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {
	private final UserService userSerice;
	
	@PostMapping("register")
	public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user){
		UserDTO userDTO = userSerice.createUser(user);
		return ResponseEntity.created(getUri()).body(
				HttpResponse.builder()
				.kodeWaktu(LocalDateTime.now().toString())
				.message("User Created")
				.status(HttpStatus.CREATED)
				.statusCode(HttpStatus.CREATED.value())
				.data(of("user", userDTO))
				.build()
				);
	}

	private URI getUri() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
	}

}
