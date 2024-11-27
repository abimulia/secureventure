/**
 * UserEvent.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 9:32:01 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class UserEvent {
	private Long id;
	private String type;
	private String description;
	private String device;
	private String ipAddress;
	private LocalDateTime createdDate;

}
