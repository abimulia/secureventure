/**
 * 
 */
package com.abimulia.secureventure.user.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 2:06:11 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Role {
	private Long id;
	private String name;
	private String permission;
}
