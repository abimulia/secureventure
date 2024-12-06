/**
 * PurchaseSqlQuery.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.query;

/**
* 
* @author abimu
*
* @version 1.0 (28-Nov-2024)
* @since 28-Nov-2024 10:47:48 AM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
public class PurchaseSqlQuery {
	//Purchase
    public static final String SELECT_PURCHASE_REQUESTS_QUERY = "SELECT * FROM PurchaseRequests";
    public static final String STATS_QUERY = "SELECT c.total_customers, i.total_invoices, inv.total_billed FROM (SELECT COUNT(*) total_customers FROM Customer) c, (SELECT COUNT(*) total_invoices FROM Invoice) i, (SELECT ROUND(SUM(total)) total_billed FROM Invoice) inv";
    public static final String INSERT_INTO_PURCHASE_QUERY ="INSERT INTO productName,,Date,productCode,) VALUES(?,?,?,?,?,?)";
    public static final String UPDATE_PURCHASE_BY_ID_QUERY = "UPDATE PurchaseRequests SET productName=?,Date=?,productCode=? WHERE id = :purchaserequestsId";
    public static final String DELETE_FROM_PURCHASE_BY_ID_QUERY = "DELETE FROM PurchaseRequests WHERE id = :prId";
    public static final String SELECT_PURCHASE_REQUEST_BY_ID_QUERY = "SELECT * FROM PurchaseRequests WHERE id = :prId";

}
