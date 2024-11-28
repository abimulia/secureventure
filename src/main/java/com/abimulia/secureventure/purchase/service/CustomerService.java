/**
 * CustomerService.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.service;

import org.springframework.data.domain.Page;

import com.abimulia.secureventure.purchase.domain.Customer;
import com.abimulia.secureventure.purchase.domain.Invoice;
import com.abimulia.secureventure.purchase.domain.Stats;

/**
* 
* @author abimu
*
* @version 1.0 (28-Nov-2024)
* @since 28-Nov-2024 12:03:45 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
public interface CustomerService {
	// Customer functions
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Page<Customer> getCustomers(int page, int size);
    Iterable<Customer> getCustomers();
    Customer getCustomer(Long id);
    Page<Customer> searchCustomers(String name, int page, int size);

    // Invoice functions
    Invoice createInvoice(Invoice invoice);
    Page<Invoice> getInvoices(int page, int size);
    void addInvoiceToCustomer(Long id, Invoice invoice);
    Invoice getInvoice(Long id);
    Stats getStats();
}
