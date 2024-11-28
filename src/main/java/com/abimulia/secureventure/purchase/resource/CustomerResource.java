/**
 * CustomerResource.java
 * 28-Nov-2024
 */
package com.abimulia.secureventure.purchase.resource;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abimulia.secureventure.domain.HttpResponse;
import com.abimulia.secureventure.purchase.domain.Customer;
import com.abimulia.secureventure.purchase.domain.Invoice;
import com.abimulia.secureventure.purchase.service.CustomerService;
import com.abimulia.secureventure.user.dto.UserDTO;
import com.abimulia.secureventure.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
* 
* @author abimu
*
* @version 1.0 (28-Nov-2024)
* @since 28-Nov-2024 1:31:27 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerResource {
	private final CustomerService customerService;
    private final UserService userService;
    
    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getCustomers(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
    	log.debug("getCustomers() user: "+user+ " page: "+ page + " size: " + size);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Customers retrieved")
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", customerService.getCustomers(page.orElse(0), size.orElse(10)),
                        "stats", customerService.getStats()))
                        .build());
    }
    
    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCustomer(@AuthenticationPrincipal UserDTO user, @RequestBody Customer customer) {
    	log.debug("createCustomer() user: "+user);
        return ResponseEntity.created(URI.create(""))
                .body(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.createCustomer(customer)))
                        .message("Customer created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
    	log.debug("getCustomer() user: "+user + "id: "+ id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.getCustomer(id)))
                        .message("Customer retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchCustomer(@AuthenticationPrincipal UserDTO user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
    	log.debug("searchCustomer() user: "+user + " page: "+ page + " size: "+ size);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.searchCustomers(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Customers retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    
    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCustomer(@AuthenticationPrincipal UserDTO user, @RequestBody Customer customer) {
    	log.debug("updateCustomer() user: "+user + " customer: "+ customer);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.updateCustomer(customer)))
                        .message("Customer updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    
    @PostMapping("/invoice/created")
    public ResponseEntity<HttpResponse> createInvoice(@AuthenticationPrincipal UserDTO user, @RequestBody Invoice invoice) {
    	log.debug("createInvoice() user: "+user + " invoice: "+ invoice);
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamps(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "invoice", customerService.createInvoice(invoice)))
                                .message("Invoice created")
                                .status(CREATED)
                                .statusCode(CREATED.value())
                                .build());
    }
    
    @PostMapping("/invoice/new")
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal UserDTO user) {
    	log.debug("newInvoice() user: "+user);
        return ResponseEntity.ok(
                        HttpResponse.builder()
                                .timeStamps(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "customers", customerService.getCustomers()))
                                .message("Customers retrieved")
                                .status(OK)
                                .statusCode(OK.value())
                                .build());
    }
    
    @GetMapping("/invoice/list")
    public ResponseEntity<HttpResponse> getInvoices(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
    	log.debug("getInvoices() user: "+user+" page: "+ page + " size: "+ size);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoices", customerService.getInvoices(page.orElse(0), size.orElse(10))))
                        .message("Invoice retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/invoice/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
    	log.debug("getInvoices() user: "+user+" id: "+ id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", customerService.getInvoice(id)))
                        .message("Invoice retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/invoice/addtocustomer/{id}")
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Invoice invoice) {
    	log.debug("addInvoiceToCustomer() user: "+user+" id: "+ id + " invoice: "+ invoice);
        customerService.addInvoiceToCustomer(id, invoice);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamps(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getCustomers()))
                        .message("Customers retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


}
