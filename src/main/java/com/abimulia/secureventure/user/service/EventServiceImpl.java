/**
 * EventServiceImpl.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.abimulia.secureventure.user.domain.UserEvent;
import com.abimulia.secureventure.user.enums.EventType;
import com.abimulia.secureventure.user.repository.EventRepository;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 10:50:08 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;

	@Override
	public Collection<UserEvent> getEventsByUserId(Long userId) {
		return eventRepository.getEventsByUserId(userId);
	}

	@Override
	public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
		eventRepository.addUserEvent(email, eventType, device, ipAddress);
	}

	@Override
	public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {
		eventRepository.addUserEvent(userId, eventType, device, ipAddress);
	}
}
