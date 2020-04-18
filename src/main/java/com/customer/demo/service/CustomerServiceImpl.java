package com.customer.demo.service;

import com.customer.demo.repository.CustomerRepository;
import com.customer.demo.model.CustomerEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    final
    CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
        return customerRepository.save(customerEntity);
    }

    @Override
    public Optional<CustomerEntity> getCustomerById(String customer_id) {
        return customerRepository.findById(customer_id);
    }
}
