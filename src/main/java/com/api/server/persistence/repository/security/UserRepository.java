package com.api.server.persistence.repository.security;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.security.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Page<User> findByRoleName(String roleName, Pageable pageable);

    Page<User> findByStatus(User.UserStatus status, Pageable pageable);

    Page<User> findByRoleNameAndStatus(String roleName, User.UserStatus status, Pageable pageable);

}
