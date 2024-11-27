/**
 * RoleService.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.service;

import java.util.Collection;

import com.abimulia.secureventure.user.domain.Role;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 10:29:08 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
public interface RoleService {
	Role getRoleByUserId(Long id);
    Collection<Role> getRoles();
}
