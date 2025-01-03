/**
 * CustomAuthorizationFilter.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static com.abimulia.secureventure.utils.ExceptionUtils.processError;

import com.abimulia.secureventure.provider.TokenProvider;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 3:28:42 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter{
	private static final String TOKEN_PREFIX = "Bearer ";
    private static final String[] PUBLIC_ROUTES = { "/user/login", "/user/verify/code", "/user/register", "/user/refresh/token", "/user/image" };
    private static final String HTTP_OPTIONS_METHOD = "OPTIONS";
    private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 try {
	            String token = getToken(request);
	            Long userId = getUserId(request);
	            if(tokenProvider.isTokenValid(userId, token)) {
	                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
	                Authentication authentication = tokenProvider.getAuthentication(userId, authorities, request);
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	            } else { SecurityContextHolder.clearContext(); }
	            filterChain.doFilter(request, response);
	        } catch (Exception exception) {
	            log.error(exception.getMessage());
	            processError(request, response, exception);
	        }
	}
	
	@Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
                request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }
	
	private Long getUserId(HttpServletRequest request) {
        return tokenProvider.getSubject(getToken(request), request);
    }

    private String getToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, EMPTY)).get();
    }

}
