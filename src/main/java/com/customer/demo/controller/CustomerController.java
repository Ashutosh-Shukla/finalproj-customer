package com.customer.demo.controller;

import com.customer.demo.model.CustomerEntity;
import com.customer.demo.service.CustomerService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@Controller
@RequestMapping(path = "/v2")
public class CustomerController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    final
    CustomerService customerService;

    final
    MeterRegistry registry;

    public CustomerController(CustomerService customerService, MeterRegistry registry) {
        this.customerService = customerService;
        this.registry = registry;
    }

    @PostMapping("/customer")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerEntity customerEntity) {
        registry.counter("custom.metrics.counter", "ApiCall", "AddCustomerPOST").increment();
        log.info("Inside post /customer mapping");
        HashMap<String, Object> entities = new HashMap<String, Object>();
        if (validateEmail(customerEntity.getEmail_address())) {
            CustomerEntity savedCustomer = customerService.createCustomer(customerEntity);
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        } else {
            entities.put("Validation Error",
                    "Please input correct email id");
        }
        return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/customer")
    public ResponseEntity<Object> getCustomer(@RequestHeader String customer_id) {
        log.info("Inside get /customer mapping");
        registry.counter("custom.metrics.counter", "ApiCall", "GetCustomer").increment();

        if (!customer_id.isEmpty()) {
            Optional<CustomerEntity> cust = customerService.getCustomerById(customer_id);
            if (cust.isPresent()) {
                return new ResponseEntity<>(cust.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/customerid")
    public ResponseEntity<Object> getCustomerID(@RequestHeader String customer_email) {
        log.info("Inside get /customerID mapping");
        registry.counter("custom.metrics.counter", "ApiCall", "GetCustomerID").increment();

        if (!customer_email.isEmpty()) {
            Optional<CustomerEntity> cust = customerService.getCustomerByEmail(customer_email);
            if (cust.isPresent()) {
                return new ResponseEntity<>(cust.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
    }

    public Boolean validateEmail(String email) {
        if (email != null || (!email.equalsIgnoreCase(""))) {
            String emailvalidator = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                    + "A-Z]{2,7}$";

            return email.matches(emailvalidator);
        }
        return Boolean.FALSE;


    }

}
