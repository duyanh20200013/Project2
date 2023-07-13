package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.NhapHangBillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NhapHangBillDetailRepository extends JpaRepository<NhapHangBillDetail, Long> {

}