package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.BanHangBillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BanHangBillDetailRepository extends JpaRepository<BanHangBillDetail, Long> {

}
