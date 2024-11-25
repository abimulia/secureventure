/**
 * 
 */
package com.abimulia.secureventure.user.repository;

import java.util.Collection;

import com.abimulia.secureventure.user.domain.User;

/**
 * 
 */
public interface UserRepository<T extends User> {
//	Basic CRUD Operations
	T create(T data);
	Collection<T> list(int page, int pageSize);
	T get(long id);
	T update(T data);
	Boolean delete(Long id);
	

}
