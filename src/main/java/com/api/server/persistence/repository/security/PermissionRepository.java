package com.api.server.persistence.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.security.GrantedPermission;

public interface PermissionRepository extends JpaRepository<GrantedPermission, Long> {

}
