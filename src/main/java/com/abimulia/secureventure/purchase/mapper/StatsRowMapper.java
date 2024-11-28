/**
 * StatsRowMapper.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abimulia.secureventure.purchase.domain.Stats;

/**
* 
* @author abimu
*
* @version 1.0 (28-Nov-2024)
* @since 28-Nov-2024 12:31:13 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
public class StatsRowMapper implements RowMapper<Stats>{

	@Override
	public Stats mapRow(ResultSet rs, int rowNum) throws SQLException {
		return Stats.builder()
                .totalCustomers(rs.getInt("total_customers"))
                .totalInvoices(rs.getInt("total_invoices"))
                .totalBilled(rs.getDouble("total_billed"))
                .build();
	}

}
