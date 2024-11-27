/**
 * SettingsForm.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 4:04:19 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
@Getter
@Setter
public class SettingsForm {
    @NotNull(message = "Enabled cannot be null or empty")
    private Boolean enabled;
    @NotNull(message = "Not Locked cannot be null or empty")
    private Boolean notLocked;
}