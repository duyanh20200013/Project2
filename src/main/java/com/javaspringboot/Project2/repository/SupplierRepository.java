package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findSupplierByPhone(String phone);

    Boolean existsByPhone(String phone);
}
