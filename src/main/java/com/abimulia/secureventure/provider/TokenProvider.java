/**
 * TokenProvider.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.provider;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.lang.System.currentTimeMillis;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.abimulia.secureventure.domain.UserPrincipal;
import com.abimulia.secureventure.user.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 3:31:37 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {
	public static final String AUTHORITIES = "authorities";
	private static final String ABI_MULIA = "ABI_MULIA";
	private static final String CUSTOMER_MANAGEMENT_SERVICE = "CUSTOMER_MANAGEMENT_SERVICE";
	private static final long ACCESS_TOKEN_EXPIRATION_TIME = 432_000_000;
	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
	public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
	@Value("${jwt.secret}")
	private String secret;
	private final UserService userService;

	public String createAccessToken(UserPrincipal userPrincipal) {
		log.debug("createAccessToken: " + userPrincipal);
		return JWT.create().withIssuer(ABI_MULIA).withAudience(CUSTOMER_MANAGEMENT_SERVICE).withIssuedAt(new Date())
				.withSubject(String.valueOf(userPrincipal.getUser().getId()))
				.withArrayClaim(AUTHORITIES, getClaimsFromUser(userPrincipal))
				.withExpiresAt(new Date(currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
				.sign(HMAC512(secret.getBytes()));

	}

	public String createRefreshToken(UserPrincipal userPrincipal) {
		log.debug("createRefreshToken: "+ userPrincipal);
		return JWT.create().withIssuer(ABI_MULIA).withAudience(CUSTOMER_MANAGEMENT_SERVICE).withIssuedAt(new Date())
				.withSubject(String.valueOf(userPrincipal.getUser().getId()))
				.withExpiresAt(new Date(currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
				.sign(HMAC512(secret.getBytes()));

	}

	public Long getSubject(String token, HttpServletRequest request) {
		log.debug("getSubject: "+ token + " req "+ request);
		try { 
			return Long.valueOf(getJWTVerifier().verify(token).getSubject());
		} catch (TokenExpiredException exception) {
			request.setAttribute("expiredMessage", exception.getMessage());
			throw exception;
		} catch (InvalidClaimException exception) {
			request.setAttribute("invalidClaim", exception.getMessage());
			throw exception;
		} catch (Exception exception) {
			throw exception;
		}
	}
	
	public List<GrantedAuthority>getAuthorities(String token) {
		log.debug("getAuthorities: "+ token);
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
    }

    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
    	log.debug("getAuthentication: "+ userId);
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(userService.getUserById(userId), null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }

    public boolean isTokenValid(Long userId, String token) {
    	log.debug("isTokenValid: "+ userId + " token: " + token);
        JWTVerifier verifier = getJWTVerifier();
        return !Objects.isNull(userId) && !isTokenExpired(verifier, token);
    }
    
    private boolean isTokenExpired(JWTVerifier verifier, String token) {
    	log.debug("isTokenExpired: "+ verifier + " token: " + token);
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

	private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
		log.debug("getClaimsFromUser: "+ userPrincipal);
		return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
	}

	private String[] getClaimsFromToken(String token) {
		log.debug("getClaimsFromToken: "+ token);
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }
	
	private JWTVerifier getJWTVerifier() {
		log.debug("getJWTVerifier");
		JWTVerifier verifier;
		try {
			Algorithm algorithm = HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer(ABI_MULIA).build();
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
		}
		return verifier;
	}

}
