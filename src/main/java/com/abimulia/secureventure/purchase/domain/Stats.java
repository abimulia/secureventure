/**
 * Stats.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.domain;

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
 * @since 28-Nov-2024 9:27:18 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
	private int totalCustomers;
	private int totalInvoices;
	private double totalBilled;
}
