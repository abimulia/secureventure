/**
 * EventRepositoryImpl.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.repository;

import java.util.Collection;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.abimulia.secureventure.user.domain.UserEvent;
import com.abimulia.secureventure.user.enums.EventType;
import com.abimulia.secureventure.user.mapper.UserEventRowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_EVENT_BY_USER_EMAIL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_EVENTS_BY_USER_ID_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_EVENT_BY_USER_ID_QUERY;
import static java.util.Map.of;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 10:53:05 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepositoryImpl implements EventRepository {
	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Collection<UserEvent> getEventsByUserId(Long userId) {
		return jdbc.query(SELECT_EVENTS_BY_USER_ID_QUERY, of("id", userId), new UserEventRowMapper());
	}

	@Override
	public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
		jdbc.update(INSERT_EVENT_BY_USER_EMAIL_QUERY, of("email", email, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));
	}

	@Override
	public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {
		jdbc.update(INSERT_EVENT_BY_USER_ID_QUERY, of("id", userId, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));// TODO Auto-generated method stub
	}

}
