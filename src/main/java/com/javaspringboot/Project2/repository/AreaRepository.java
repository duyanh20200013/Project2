package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    Boolean existsByName(String name);

    Area findAreaByName(String name);

    //Boolean existsByPhone(String phone);
}
