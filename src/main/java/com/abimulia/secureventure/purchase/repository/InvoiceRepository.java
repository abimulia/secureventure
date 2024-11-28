/**
 * InvoiceRepository.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.abimulia.secureventure.purchase.domain.Invoice;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 12:09:01 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public interface InvoiceRepository
		extends PagingAndSortingRepository<Invoice, Long>, ListCrudRepository<Invoice, Long> {

}
