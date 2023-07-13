package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.NhapHangBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NhapHangBillRepository extends JpaRepository<NhapHangBill, Long> {

}
