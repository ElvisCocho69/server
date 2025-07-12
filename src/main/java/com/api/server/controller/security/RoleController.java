package com.api.server.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.security.SaveRoleWithPermissions;
import com.api.server.dto.security.ShowRoles;
import com.api.server.service.security.RoleService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<ShowRoles>> getAllRoles(Pageable pageable) {
        try {
            Page<ShowRoles> roles = roleService.findAll(pageable); // Aquí se pasa pageable
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoleWithPermissions(
            @RequestBody @Valid SaveRoleWithPermissions saveRoleWithPermissions) {

        try {
            roleService.createRoleWithPermissions(saveRoleWithPermissions);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rol creado y permisos asignados.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el rol: " + e.getMessage());
        }

    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRoleWithPermissions(
            @PathVariable Long roleId,
            @RequestBody @Valid SaveRoleWithPermissions updateRoleRequest) {
        try {
            roleService.updateRoleWithPermissions(roleId, updateRoleRequest);
            return ResponseEntity.ok("Rol actualizado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el rol: " + e.getMessage());
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long roleId) {
        try {
            roleService.deleteRoleById(roleId);
            return ResponseEntity.ok("Rol eliminado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el rol: " + e.getMessage());
        }
    }

}
