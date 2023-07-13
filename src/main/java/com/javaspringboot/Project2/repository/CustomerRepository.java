package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.Customer;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerByPhone(String phone);

   Boolean existsByPhone(String phone);
}
