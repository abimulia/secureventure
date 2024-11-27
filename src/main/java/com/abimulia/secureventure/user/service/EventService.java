/**
 * EventService.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.service;

import java.util.Collection;

import com.abimulia.secureventure.user.domain.UserEvent;
import com.abimulia.secureventure.user.enums.EventType;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 10:48:49 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public interface EventService {
	Collection<UserEvent> getEventsByUserId(Long userId);

	void addUserEvent(String email, EventType eventType, String device, String ipAddress);

	void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);
}
