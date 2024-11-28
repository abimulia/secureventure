/**
 * PurchaseRequestRepository.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.repository;

import java.util.List;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 10:30:18 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public interface PurchaseRequestRepository<T> {
	List<T> list();

	T create(T data);

	T get(Long id);

	void update(T t, Long id);

	boolean delete(Long id);

}
