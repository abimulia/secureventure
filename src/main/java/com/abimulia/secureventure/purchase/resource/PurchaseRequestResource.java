/**
 * PurchaseRequestResource.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.resource;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abimulia.secureventure.purchase.domain.PurchaseRequests;
import com.abimulia.secureventure.purchase.service.PurchaseRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 1:45:20 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@RestController
@RequestMapping(path = "/purcharserequets")
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestResource {
	private final PurchaseRequestService purchaseRequestsService;

	@GetMapping(path = "/{id}")
	public ResponseEntity<PurchaseRequests> findById(@PathVariable Long id) {
		log.debug("findById() id: " + id);
		return ResponseEntity.ok(purchaseRequestsService.getPurchaseRequestById(id));
	}

	@GetMapping(path = "/")
	public ResponseEntity<List<PurchaseRequests>> findAll() {
		log.debug("findAll()");
		return ResponseEntity.ok(purchaseRequestsService.getAllPurchaseRequests());
	}

}
