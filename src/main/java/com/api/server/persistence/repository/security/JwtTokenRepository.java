package com.api.server.persistence.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.security.JwtToken;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findByToken(String jwt);

}
