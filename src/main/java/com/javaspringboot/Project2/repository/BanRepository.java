package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
    Boolean existsByName(String name);
}
