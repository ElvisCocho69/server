package com.api.server.service.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.api.server.dto.auth.AuthenticationRequest;
import com.api.server.dto.auth.AuthenticationResponse;
import com.api.server.dto.security.RegisteredUser;
import com.api.server.dto.security.SaveUser;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.exception.UserInactiveException;
import com.api.server.persistence.entity.security.JwtToken;
import com.api.server.persistence.entity.security.User;
import com.api.server.persistence.repository.security.JwtTokenRepository;
import com.api.server.service.security.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepository jwtRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisteredUser registerOneUser(SaveUser newUser) {

        User user = userService.registerOneUser(newUser);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveUserToken(user, jwt);

        RegisteredUser userDto = new RegisteredUser();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());

        userDto.setJwt(jwt);

        return userDto;

    }

    private Map<String, Object> generateExtraClaims(User user) {

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;

    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        try {
            Optional<User> userOptional = userService.findOneByUsername(authenticationRequest.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getStatus() == User.UserStatus.DISABLED) {
                    throw new UserInactiveException("Usuario " + user.getUsername() + " está desactivado");
                }
            } else {
                throw new UsernameNotFoundException("El usuario " + authenticationRequest.getUsername() + " no existe en el sistema");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword());

            authenticationManager.authenticate(authentication);

            UserDetails user = userService.findOneByUsername(authenticationRequest.getUsername()).get();
            String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));
            saveUserToken((User) user, jwt);

            AuthenticationResponse authRsp = new AuthenticationResponse();
            authRsp.setJwt(jwt);

            return authRsp;
        } catch (UsernameNotFoundException e) {
            throw e; 
        }
    }

    private void saveUserToken(User user, String jwt) {

        JwtToken token = new JwtToken();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setValid(true);

        jwtRepository.save(token);

    }

    public boolean validateToken(String jwt) {

        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public User findLoggedInUser() {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();

        String username = (String) auth.getPrincipal();

        return userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. USERNAME: " + username));

    }

    public void logout(HttpServletRequest request) {

        String jwt = jwtService.extractJwtFromRequest(request);

        if (jwt == null || !StringUtils.hasText(jwt))
            return;

        Optional<JwtToken> token = jwtRepository.findByToken(jwt);

        if (token.isPresent() && token.get().isValid()) {
            token.get().setValid(false);
            jwtRepository.save(token.get());
        }

    }

}
