package com.javaspringboot.Project2.repository;

import com.javaspringboot.Project2.enumm.ERole;
import com.javaspringboot.Project2.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
