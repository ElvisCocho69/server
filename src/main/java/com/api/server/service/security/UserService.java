package com.api.server.service.security;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.dto.security.RegisteredUser;
import com.api.server.dto.security.SaveUser;
import com.api.server.dto.security.ChangePassword;
import com.api.server.persistence.entity.security.User;

public interface UserService {

    Page<RegisteredUser> findAll(String role, String status, Pageable pageable);

    User registerOneUser(SaveUser newUser);
 
    Optional<User> findOneByUsername(String username);

    RegisteredUser findById(Long id);

    RegisteredUser changeOwnPassword(ChangePassword user);

    RegisteredUser changePassword(Long id, ChangePassword user);

    RegisteredUser disableUser(Long id);

    RegisteredUser updateUser(Long id, SaveUser user);
}