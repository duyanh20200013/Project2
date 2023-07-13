package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.enumm.EStatusBill;
import com.javaspringboot.Project2.model.BanHangBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BanHangBillRepository extends JpaRepository<BanHangBill, Long> {
    List<BanHangBill> findBanHangBillByStatus(EStatusBill status);
}
