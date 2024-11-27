/**
 * NewUserEventListener.java
 * 27-Nov-2024
 */
package com.abimulia.secureventure.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.abimulia.secureventure.user.event.NewUserEvent;
import com.abimulia.secureventure.user.service.EventService;
import static com.abimulia.secureventure.utils.RequestUtils.getDevice;
import static com.abimulia.secureventure.utils.RequestUtils.getIpAddress;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
* 
* @author abimu
*
* @version 1.0 (27-Nov-2024)
* @since 27-Nov-2024 12:08:37 AM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/

@Component
@RequiredArgsConstructor
public class NewUserEventListener {
	private final EventService eventService;
    private final HttpServletRequest request;

    @EventListener
    public void onNewUserEvent(NewUserEvent event) {
        eventService.addUserEvent(event.getEmail(), event.getType(), getDevice(request), getIpAddress(request));
    }
}
