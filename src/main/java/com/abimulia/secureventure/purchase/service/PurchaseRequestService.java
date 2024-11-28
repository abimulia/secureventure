/**
 * PurchaseRequestService.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.service;

import java.util.List;

import com.abimulia.secureventure.purchase.domain.PurchaseRequests;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 11:55:22 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public interface PurchaseRequestService {
	Object createPurchaseRequest(PurchaseRequests purchaseRequests);

	List<PurchaseRequests> getAllPurchaseRequests();

	PurchaseRequests getPurchaseRequestById(Long id);

	boolean deletePurchaseRequests(Long id);

}
