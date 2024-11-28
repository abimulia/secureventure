/**
 * CustomerServiceImpl.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.service;

import static com.abimulia.secureventure.purchase.query.PurchaseSqlQuery.STATS_QUERY;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.data.domain.PageRequest.of;

import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.abimulia.secureventure.purchase.domain.Customer;
import com.abimulia.secureventure.purchase.domain.Invoice;
import com.abimulia.secureventure.purchase.domain.Stats;
import com.abimulia.secureventure.purchase.mapper.StatsRowMapper;
import com.abimulia.secureventure.purchase.repository.CustomerRepository;
import com.abimulia.secureventure.purchase.repository.InvoiceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 12:04:55 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	private final CustomerRepository customerRepository;
	private final InvoiceRepository invoiceRepository;
	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Customer createCustomer(Customer customer) {
		log.debug("createCustomer() + customer: " + customer);
		customer.setCreatedDate(new Date());
		return customerRepository.save(customer);
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		log.debug("updateCustomer() + customer: " + customer);
		return customerRepository.save(customer);
	}

	@Override
	public Page<Customer> getCustomers(int page, int size) {
		log.debug("getCustomers() page: " + page + " size: " + size);
		return customerRepository.findAll(of(page, size));
	}

	@Override
	public Iterable<Customer> getCustomers() {
		log.debug("getCustomers()");
		return customerRepository.findAll();
	}

	@Override
	public Customer getCustomer(Long id) {
		log.debug("getCustomer() id: " + id);
		return customerRepository.findById(id).get();
	}

	@Override
	public Page<Customer> searchCustomers(String name, int page, int size) {
		log.debug("searchCustomers() name: " + name + " page: " + page + " size: " + size);
		return customerRepository.findByNameContaining(name, of(page, size));
	}

	@Override
	public Invoice createInvoice(Invoice invoice) {
		log.debug("createInvoice() invoice: " + invoice);
		invoice.setInvoiceNumber(randomAlphanumeric(8).toUpperCase());
		return invoiceRepository.save(invoice);
	}

	@Override
	public Page<Invoice> getInvoices(int page, int size) {
		log.debug("getInvoices() page: " + page + " size: " + size);
		return invoiceRepository.findAll(of(page, size));
	}

	@Override
	public void addInvoiceToCustomer(Long id, Invoice invoice) {
		log.debug("addInvoiceToCustomer() id: " + id + " invoice: " + invoice);
		invoice.setInvoiceNumber(randomAlphanumeric(8).toUpperCase());
		Customer customer = customerRepository.findById(id).get();
		invoice.setCustomer(customer);
		invoiceRepository.save(invoice);

	}

	@Override
	public Invoice getInvoice(Long id) {
		log.debug("getInvoice() id: " + id);
		return invoiceRepository.findById(id).get();
	}

	@Override
	public Stats getStats() {
		log.debug("getStats()");
		return jdbc.queryForObject(STATS_QUERY, Map.of(), new StatsRowMapper());
	}

}
