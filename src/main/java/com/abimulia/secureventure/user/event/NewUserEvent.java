/**
 * NewUserEvent.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.event;

import org.springframework.context.ApplicationEvent;

import com.abimulia.secureventure.user.enums.EventType;

import lombok.Getter;
import lombok.Setter;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 10:42:53 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/

@Getter
@Setter
public class NewUserEvent extends ApplicationEvent{
	private EventType type;
    private String email;

    public NewUserEvent(String email, EventType type) {
        super(email);
        this.type = type;
        this.email = email;
    }

}
