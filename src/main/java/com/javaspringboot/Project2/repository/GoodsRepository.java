package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Boolean existsByName(String name);
}
