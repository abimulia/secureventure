/**
 * 
 */
package com.abimulia.secureventure.user.repository;

import java.util.Collection;

import com.abimulia.secureventure.user.domain.Role;

/**
 * 
 */
public interface RoleRepository<T extends Role> {
//	Basic CRUD Operations
	T create(T data);
	Collection<T> list(int page, int pageSize);
	T get(long id);
	T update(T data);
	Boolean delete(Long id);
	
	/* More Complex Operation */
	void addRoleToUser(Long userId, String roleName);
	
	Role getRoleByUserId(Long userId);
	Role getRoleByUserEmail(String email);
	void updateUserRole(Long userId, String roleName);
	
}
