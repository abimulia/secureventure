/**
 * 
 */
package com.abimulia.secureventure.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import com.abimulia.secureventure.filter.CustomAuthorizationFilter;
import com.abimulia.secureventure.handler.CustomAccessDeniedHandler;
import com.abimulia.secureventure.handler.CustomAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 3:18:31 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig{
	private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    
    private static final String[] PUBLIC_URLS = { "/user/verify/password/**",
            "/user/login/**", "/user/verify/code/**", "/user/register/**", "/user/resetpassword/**", "/user/verify/account/**",
            "/user/refresh/token/**", "/user/image/**","user/list/**" };
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//		log.debug("configure() " + httpSecurity);
//		CsrfTokenRequestAttributeHandler tokenRequestHandler = new CsrfTokenRequestAttributeHandler();
//		tokenRequestHandler.setCsrfRequestAttributeName(null);
//		httpSecurity
//				.authorizeHttpRequests(
//						authorize -> authorize.requestMatchers(PUBLIC_URLS).permitAll()
//							.requestMatchers(OPTIONS).permitAll()
//							.requestMatchers(DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER")
//							.requestMatchers(DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER")
//							.anyRequest().authenticated())
//				.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//						.csrfTokenRequestHandler(tokenRequestHandler))
//				.addFilterBefore(customAuthorizationFilter,UsernamePasswordAuthenticationFilter.class);
//		log.debug("httpSecurity: " + httpSecurity);
//		return httpSecurity.build();
		httpSecurity.csrf().disable().cors();
		httpSecurity.sessionManagement().sessionCreationPolicy(STATELESS);
		httpSecurity.authorizeHttpRequests().requestMatchers(PUBLIC_URLS).permitAll();
		httpSecurity.authorizeHttpRequests().requestMatchers(OPTIONS).permitAll(); // Not needed
		httpSecurity.authorizeHttpRequests().requestMatchers(DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER");
		httpSecurity.authorizeHttpRequests().requestMatchers(DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER");
		httpSecurity.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint);
		httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
		httpSecurity.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
	        return httpSecurity.build();
	}
	
	@Bean
    public AuthenticationManager authenticationManager() {
		log.debug("authenticationManager");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
	
}
