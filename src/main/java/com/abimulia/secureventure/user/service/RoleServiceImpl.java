/**
 * RoleServiceImpl.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.abimulia.secureventure.user.domain.Role;
import com.abimulia.secureventure.user.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 10:30:22 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	private final RoleRepository<Role> roleRepository;

	@Override
	public Role getRoleByUserId(Long id) {
		return roleRepository.getRoleByUserId(id);
	}

	@Override
	public Collection<Role> getRoles() {
		return roleRepository.list();
	}

}
