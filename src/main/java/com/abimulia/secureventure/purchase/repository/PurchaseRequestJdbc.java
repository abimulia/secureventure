/**
 * PurchaseRequestJdbc.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.repository;

import static com.abimulia.secureventure.purchase.query.PurchaseSqlQuery.DELETE_FROM_PURCHASE_BY_ID_QUERY;
import static com.abimulia.secureventure.purchase.query.PurchaseSqlQuery.INSERT_INTO_PURCHASE_QUERY;
import static com.abimulia.secureventure.purchase.query.PurchaseSqlQuery.SELECT_PURCHASE_REQUESTS_QUERY;
import static com.abimulia.secureventure.purchase.query.PurchaseSqlQuery.SELECT_PURCHASE_REQUEST_BY_ID_QUERY;
import static com.abimulia.secureventure.purchase.query.PurchaseSqlQuery.UPDATE_PURCHASE_BY_ID_QUERY;
import static java.util.Map.of;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.abimulia.secureventure.exception.ApiException;
import com.abimulia.secureventure.purchase.domain.PurchaseRequests;
import com.abimulia.secureventure.purchase.mapper.PurchaseRequestRowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 10:32:42 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestJdbc implements PurchaseRequestRepository<PurchaseRequests> {
	private JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public List<PurchaseRequests> list() {
		log.debug("list()");
		try {
			List<PurchaseRequests> purchaseRequests = jdbc.query(SELECT_PURCHASE_REQUESTS_QUERY,
					new PurchaseRequestRowMapper()); // query(query, new UserRowMapper());
			return purchaseRequests;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ApiException("An error occurred while retrieving the list of users. Please try again.", ex);
		}
	}

	@Override
	public PurchaseRequests create(PurchaseRequests purchaseRequests) {
		log.debug("create() purchaseRequest: " + purchaseRequests);
		int insert = jdbcTemplate.update(INSERT_INTO_PURCHASE_QUERY, purchaseRequests.getReceiverEmail(),
				purchaseRequests.getProductName(), purchaseRequests.getDate(), purchaseRequests.getProductCode());
		log.info(insert + " row(s) affected");
		return purchaseRequests;
	}

	@Override
	public PurchaseRequests get(Long id) {
		log.debug("get() id: " + id);
		try {

			return jdbc.queryForObject(SELECT_PURCHASE_REQUEST_BY_ID_QUERY, of("prId", id),
					new PurchaseRequestRowMapper());

		} catch (EmptyResultDataAccessException ex) {
			log.error(ex.getMessage());
			throw new ApiException("No PURCHASE REQUESTS found by id: " + id, ex);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ApiException("Failed to get purchase request by id: " + id, ex);
		}
	}

	@Override
	public void update(PurchaseRequests purchaseRequests, Long id) {
		log.debug("update() purchaseRequest: " + purchaseRequests + " by id: " + id);
		try {

			jdbcTemplate.update(UPDATE_PURCHASE_BY_ID_QUERY, purchaseRequests.getProductName(),
					purchaseRequests.getDate(), purchaseRequests.getProductCode(), id);
			return;

		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ApiException("Failed to update PurchaseRequest", ex);
		}
	}

	@Override
	public boolean delete(Long id) {
		log.debug("delete() id: " + id);
		try {

			jdbc.update(DELETE_FROM_PURCHASE_BY_ID_QUERY, Collections.singletonMap("prId", id));
			return true;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ApiException("Failed to delete PurchaseRequest", ex);
		}

	}

}
