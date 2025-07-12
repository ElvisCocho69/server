package com.api.server.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.api.server.dto.security.ChangePassword;
import com.api.server.dto.security.RegisteredUser;
import com.api.server.dto.security.SaveUser;
import com.api.server.exception.InvalidPasswordException;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.security.Role;
import com.api.server.persistence.entity.security.User;
import com.api.server.persistence.repository.security.UserRepository;
import com.api.server.service.security.RoleService;
import com.api.server.service.security.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public Page<RegisteredUser> findAll(String role, String status, Pageable pageable) {
        Page<User> users;
        
        if (role != null && status != null) {
            users = userRepository.findByRoleNameAndStatus(role, User.UserStatus.valueOf(status), pageable);
        } else if (role != null) {
            users = userRepository.findByRoleName(role, pageable);
        } else if (status != null) {
            users = userRepository.findByStatus(User.UserStatus.valueOf(status), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        
        return users.map(user -> {
            RegisteredUser dto = new RegisteredUser();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setLastname(user.getLastname());
            dto.setEmail(user.getEmail());
            dto.setContacto(user.getContacto());
            dto.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
            dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
            return dto;
        });
    }

    @Override
    public User registerOneUser(SaveUser newUser) {
        validatePassword(newUser);

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setLastname(newUser.getLastname());
        user.setEmail(newUser.getEmail());
        user.setContacto(newUser.getContacto());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setStatus(User.UserStatus.valueOf(newUser.getStatus()));

        Role role = null;
        if (newUser.getRole() != null && !newUser.getRole().isEmpty()) {
            role = roleService.findByName(newUser.getRole())
                    .orElseThrow(() -> new ObjectNotFoundException("Role not found: " + newUser.getRole()));
        } else {
            role = roleService.findDefaultRole()
                    .orElseThrow(() -> new ObjectNotFoundException("Role not found. Default Role"));
        }
        user.setRole(role);

        return userRepository.save(user);
    }

    private void validatePassword(SaveUser dto) {
        if (!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Las contraseñas no pueden estar vacías");
        }

        if (!dto.getPassword().equals(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Las contraseñas no coinciden");
        }

        if ("Cliente".equals(dto.getRole())) {
            if (dto.getPassword().length() < 8) {
                throw new InvalidPasswordException("La contraseña debe tener al menos 8 caracteres");
            }
            return;
        }

        if (dto.getPassword().length() < 8) {
            throw new InvalidPasswordException("La contraseña debe tener al menos 8 caracteres");
        }

        if (!dto.getPassword().matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("La contraseña debe contener al menos una letra mayúscula");
        }

        if (!dto.getPassword().matches(".*[0-9].*")) {
            throw new InvalidPasswordException("La contraseña debe contener al menos un número");
        }

        if (!dto.getPassword().matches(".*[!@#$%^&*()_+\\-=\\[\\]{};'\\\"\\\\|,.<>\\/?].*")) {
            throw new InvalidPasswordException("La contraseña debe contener al menos un símbolo especial");
        }

        if (dto.getPassword().matches(".*\\s.*")) {
            throw new InvalidPasswordException("La contraseña no puede contener espacios en blanco");
        }

        if (dto.getPassword().contains(dto.getUsername())) {
            throw new InvalidPasswordException("La contraseña no puede contener el nombre de usuario");
        }

        if (dto.getPassword().contains(dto.getName())) {
            throw new InvalidPasswordException("La contraseña no puede contener el nombre");
        }

        if (dto.getPassword().contains(dto.getLastname())) {
            throw new InvalidPasswordException("La contraseña no puede contener el apellido");
        }
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public RegisteredUser findById(Long id) {
        return userRepository.findById(id).map(user -> {
            RegisteredUser dto = new RegisteredUser();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setLastname(user.getLastname());
            dto.setEmail(user.getEmail());
            dto.setContacto(user.getContacto());
            dto.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
            dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
            return dto;
        }).orElse(null);
    }

    @Override
    public RegisteredUser changePassword(Long id, ChangePassword user) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ObjectNotFoundException("Current user not found"));
        
        // Check if current user is admin
        boolean isAdmin = currentUser.getRole().getName().equals("Administrador");
        
        // Validate password change based on user role
        validatePasswordChange(user, userToUpdate, isAdmin);
        
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userToUpdate);
        return findById(id);
    }

    @Override
    public RegisteredUser changeOwnPassword(ChangePassword user) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ObjectNotFoundException("Current user not found"));
        
        // Validate password change (always requires old password for own password change)
        validatePasswordChange(user, currentUser, false);
        
        currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(currentUser);
        return findById(currentUser.getId());
    }

    private void validatePasswordChange(ChangePassword dto, User existingUser, boolean isAdmin) {
        // If not admin, validate current password
        if (!isAdmin) {
            if (!StringUtils.hasText(dto.getOldPassword())) {
                throw new InvalidPasswordException("La contraseña actual es requerida");
            }

            if (!passwordEncoder.matches(dto.getOldPassword(), existingUser.getPassword())) {
                throw new InvalidPasswordException("La contraseña actual es incorrecta");
            }
        }

        if (!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Las contraseñas no pueden estar vacías");
        }

        if (!dto.getPassword().equals(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Las contraseñas no coinciden");
        }

        // Si es un cliente, solo validamos longitud mínima
        if ("Cliente".equals(existingUser.getRole().getName())) {
            if (dto.getPassword().length() < 8) {
                throw new InvalidPasswordException("La contraseña debe tener al menos 8 caracteres");
            }
            return;
        }

        // Para otros roles, mantenemos todas las validaciones
        if (dto.getPassword().length() < 8) {
            throw new InvalidPasswordException("La contraseña debe tener al menos 8 caracteres");
        }

        if (!dto.getPassword().matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("La contraseña debe contener al menos una letra mayúscula");
        }

        if (!dto.getPassword().matches(".*[0-9].*")) {
            throw new InvalidPasswordException("La contraseña debe contener al menos un número");
        }

        if (!dto.getPassword().matches(".*[!@#$%^&*()_+\\-=\\[\\]{};'\\\"\\\\|,.<>\\/?].*")) {
            throw new InvalidPasswordException("La contraseña debe contener al menos un símbolo especial");
        }

        if (dto.getPassword().matches(".*\\s.*")) {
            throw new InvalidPasswordException("La contraseña no puede contener espacios en blanco");
        }

        if (dto.getPassword().contains(existingUser.getUsername())) {
            throw new InvalidPasswordException("La contraseña no puede contener el nombre de usuario");
        }

        if (dto.getPassword().contains(existingUser.getName())) {
            throw new InvalidPasswordException("La contraseña no puede contener el nombre");
        }

        if (dto.getPassword().contains(existingUser.getLastname())) {
            throw new InvalidPasswordException("La contraseña no puede contener el apellido");
        }
    }

    @Override
    public RegisteredUser disableUser(Long id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        userToUpdate.setStatus(User.UserStatus.DISABLED);
        userRepository.save(userToUpdate);
        return findById(id);
    }

    @Override
    public RegisteredUser updateUser(Long id, SaveUser user) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        if (StringUtils.hasText(user.getUsername())) {
            userToUpdate.setUsername(user.getUsername());
        }
        if (StringUtils.hasText(user.getName())) {
            userToUpdate.setName(user.getName());
        }
        if (StringUtils.hasText(user.getLastname())) {
            userToUpdate.setLastname(user.getLastname());
        }
        if (StringUtils.hasText(user.getEmail())) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (StringUtils.hasText(user.getContacto())) {
            userToUpdate.setContacto(user.getContacto());
        }
        if (StringUtils.hasText(user.getRole())) {
            userToUpdate.setRole(roleService.findByName(user.getRole()).orElseThrow(() -> new ObjectNotFoundException("Rol no encontrado: " + user.getRole())));
        }
        if (StringUtils.hasText(user.getStatus())) {
            userToUpdate.setStatus(User.UserStatus.valueOf(user.getStatus()));
        }
        
        userRepository.save(userToUpdate);
        return findById(id);
    }
    
    
}
