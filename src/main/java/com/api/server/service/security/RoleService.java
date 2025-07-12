package com.api.server.service.security;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.dto.security.SaveRoleWithPermissions;
import com.api.server.dto.security.ShowRoles;
import com.api.server.persistence.entity.security.Role;

public interface RoleService {

    Optional<Role> findDefaultRole();

    Page<ShowRoles> findAll(Pageable pageable);

    void createRoleWithPermissions(SaveRoleWithPermissions saveRoleWithPermissions);
    
    void updateRoleWithPermissions(Long roleId, SaveRoleWithPermissions updateRoleRequest);
    
    void deleteRoleById(Long roleId);

    Optional<Role> findByName(String role);
}
