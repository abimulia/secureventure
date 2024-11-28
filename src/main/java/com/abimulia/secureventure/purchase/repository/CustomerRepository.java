/**
 * CustomerRepository.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.abimulia.secureventure.purchase.domain.Customer;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 12:06:32 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public interface CustomerRepository
		extends PagingAndSortingRepository<Customer, Long>, ListCrudRepository<Customer, Long> {
	Page<Customer> findByNameContaining(String name, Pageable pageable);

}
