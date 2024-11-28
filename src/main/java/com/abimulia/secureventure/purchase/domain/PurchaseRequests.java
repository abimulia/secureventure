/**
 * PurchaseRequests.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
* 
* @author abimu
*
* @version 1.0 (28-Nov-2024)
* @since 28-Nov-2024 9:33:46 AM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class PurchaseRequests {
	private Long id;;
    private String productName;
    private Date date;
    private String productCode;
    private int Quantity;
    private String receiverEmail;
}
