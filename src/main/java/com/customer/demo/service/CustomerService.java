package com.customer.demo.service;

import com.customer.demo.model.CustomerEntity;

import java.util.Optional;

public interface CustomerService {
    CustomerEntity createCustomer(CustomerEntity customerEntity);

    Optional<CustomerEntity> getCustomerById(String customer_id);
}
