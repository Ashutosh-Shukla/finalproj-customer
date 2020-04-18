package com.customer.demo.repository;

import com.customer.demo.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository  extends JpaRepository<CustomerEntity, String> {
}
