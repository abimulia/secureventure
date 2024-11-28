/**
 * PurchaseRequestRowMapper.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abimulia.secureventure.purchase.domain.PurchaseRequests;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 10:37:09 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public class PurchaseRequestRowMapper implements RowMapper<PurchaseRequests> {

	@Override
	public PurchaseRequests mapRow(ResultSet rs, int rowNum) throws SQLException {
		PurchaseRequests PurchaseRequests = new PurchaseRequests();
		PurchaseRequests purchaseRequests = new PurchaseRequests();
		purchaseRequests.setId(rs.getLong("pr_id"));
		purchaseRequests.setProductName(rs.getString("productName"));
		purchaseRequests.setDate(rs.getDate(String.valueOf(rs.getDate("Date"))));
		purchaseRequests.setProductCode(rs.getString("productCode"));
		purchaseRequests.setQuantity(rs.getInt("quantity"));
		purchaseRequests.setReceiverEmail(rs.getString("receiverEmail"));
		return PurchaseRequests;
	}
}
