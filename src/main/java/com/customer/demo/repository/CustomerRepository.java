package com.customer.demo.repository;

import com.customer.demo.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository  extends JpaRepository<CustomerEntity, String> {

    @Query(value = "SELECT * FROM customer c WHERE c.email_address=?1 limit 1", nativeQuery = true)
    Optional<CustomerEntity> findByEmail(String customer_email);
}
