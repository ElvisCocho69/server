package com.api.server.persistence.repository.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String defaultRole);
    
    List<Role> findAllByName(String name);
}
