/**
 * PurchaseRequestServiceImpl.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abimulia.secureventure.purchase.domain.PurchaseRequests;
import com.abimulia.secureventure.purchase.repository.PurchaseRequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 11:56:36 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestServiceImpl implements PurchaseRequestService {
	private final PurchaseRequestRepository purchaseRequestRepository;

	@Override
	public Object createPurchaseRequest(PurchaseRequests purchaseRequests) {
		log.debug("createPurchaseRequest() purchaseRequest: " + purchaseRequests);
		return purchaseRequestRepository.create(purchaseRequests);
	}

	@Override
	public List<PurchaseRequests> getAllPurchaseRequests() {
		log.debug("getAllPurchaseRequests()");
		return purchaseRequestRepository.list();
	}

	@Override
	public PurchaseRequests getPurchaseRequestById(Long prId) {
		log.debug("getPurchaseRequestById() id: " + prId);
		return (PurchaseRequests) purchaseRequestRepository.get(prId);
	}

	@Override
	public boolean deletePurchaseRequests(Long prId) {
		log.debug("deletePurchaseRequests() prId: " + prId);
		return purchaseRequestRepository.delete(prId);
	}

}
