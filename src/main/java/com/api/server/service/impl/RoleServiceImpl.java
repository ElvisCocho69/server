package com.api.server.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.server.dto.security.SaveRoleWithPermissions;
import com.api.server.dto.security.ShowRoles;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.security.GrantedPermission;
import com.api.server.persistence.entity.security.Operation;
import com.api.server.persistence.entity.security.Role;
import com.api.server.persistence.repository.security.OperationRepository;
import com.api.server.persistence.repository.security.PermissionRepository;
import com.api.server.persistence.repository.security.RoleRepository;
import com.api.server.service.security.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Value("${security.default.role}")
    private String defaultRole;

    @Override
    public Optional<Role> findDefaultRole() {

        return roleRepository.findByName(defaultRole);

    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Page<ShowRoles> findAll(Pageable pageable) {
        // Obtener los roles paginados desde el repositorio
        return roleRepository.findAll(pageable).map(this::mapRoleToShowDto);
    }

    // Método para mapear un role a un DTO
    private ShowRoles mapRoleToShowDto(Role role) {
        ShowRoles showDto = new ShowRoles();
        showDto.setId(role.getId());
        showDto.setName(role.getName());

        // Extraer permisos relacionados con el rol
        List<String> permissions = role.getPermissions().stream()
                .map(grantedPermission -> grantedPermission.getOperation().getName()) // Obtener el nombre de la operación
                .collect(Collectors.toList());

        showDto.setPermissions(permissions);
        return showDto;
    }

    @Override
    public void createRoleWithPermissions(SaveRoleWithPermissions saveRoleWithPermissions) {
        // Verificar si el rol ya existe
        if (roleRepository.findByName(saveRoleWithPermissions.getName()).isPresent()) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + saveRoleWithPermissions.getName());
        }

        Role role = new Role();
        role.setName(saveRoleWithPermissions.getName());
        roleRepository.save(role);

        for (Long operationId : saveRoleWithPermissions.getOperationIds()) {
            Operation operation = operationRepository.findById(operationId)
                    .orElseThrow(() -> new RuntimeException("Operación no encontrada. ID: " + operationId));

            // Asocia el permiso con el rol
            GrantedPermission grantedPermission = new GrantedPermission();
            grantedPermission.setRole(role);
            grantedPermission.setOperation(operation);

            permissionRepository.save(grantedPermission);
        }
    }

    @Override
    public void updateRoleWithPermissions(Long roleId, SaveRoleWithPermissions updateRoleRequest) {
        // Buscar el rol existente
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ObjectNotFoundException("Rol no encontrado: " + roleId));

        // Verificar si el nuevo nombre ya existe en otro rol
        if (!existingRole.getName().equals(updateRoleRequest.getName()) && 
            roleRepository.findByName(updateRoleRequest.getName()).isPresent()) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + updateRoleRequest.getName());
        }

        // Actualizar el nombre del rol
        existingRole.setName(updateRoleRequest.getName());
        
        // Eliminar todos los permisos existentes
        permissionRepository.deleteAll(existingRole.getPermissions());
        existingRole.getPermissions().clear();
        
        // Crear nuevos permisos
        for (Long operationId : updateRoleRequest.getOperationIds()) {
            Operation operation = operationRepository.findById(operationId)
                    .orElseThrow(() -> new ObjectNotFoundException("Operation not found with ID: " + operationId));

            GrantedPermission grantedPermission = new GrantedPermission();
            grantedPermission.setRole(existingRole);
            grantedPermission.setOperation(operation);

            permissionRepository.save(grantedPermission);
            existingRole.getPermissions().add(grantedPermission);
        }

        roleRepository.save(existingRole);
    }

    @Override
    public void deleteRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ObjectNotFoundException("Role not found. ID: " + roleId));
        roleRepository.delete(role);
    }

}
