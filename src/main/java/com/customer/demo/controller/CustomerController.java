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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
@RequestMapping(path = "/v1")
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

    public Boolean validateEmail(String email) {
        if (email != null || (!email.equalsIgnoreCase(""))) {
            String emailvalidator = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                    + "A-Z]{2,7}$";

            return email.matches(emailvalidator);
        }
        return Boolean.FALSE;


    }

}
