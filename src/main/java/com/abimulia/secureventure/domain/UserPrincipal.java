/**
 * UserPrincipal.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.domain;

import static com.abimulia.secureventure.user.mapper.UserDTOMapper.fromUser;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.abimulia.secureventure.user.domain.Role;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 3:39:13 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
	private final User user;
	private final Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return stream(this.role.getPermission().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.user.isNotLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isEnabled();
	}

	public UserDTO getUser() {
		return fromUser(this.user, role);
	}
}
