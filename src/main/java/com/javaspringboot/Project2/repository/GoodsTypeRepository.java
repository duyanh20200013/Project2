package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.GoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsTypeRepository extends JpaRepository<GoodsType, Long> {
    Boolean existsByName(String name);

    GoodsType findGoodsTypeByName(String name);

    //Boolean existsByPhone(String phone);
}
