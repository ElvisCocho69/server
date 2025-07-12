package com.api.server.service.auth;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + EXPIRATION_IN_MINUTES * 60 * 1000);

        String jwt = Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(extraClaims)
                .signWith(generatedKey(), Jwts.SIG.HS256)
                .compact();

        return jwt;

    }

    private SecretKey generatedKey() {

        byte[] passwordDecoded = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(passwordDecoded);

    }

    public String extractUsername(String jwt) {

        return extractAllClaims(jwt).getSubject();

    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generatedKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        
        String authorizationHeader = request.getHeader("Authorization"); // Bearer jwt
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {           
            return null;
        }
        
        return authorizationHeader.split(" ")[1];

    }

    public Date extractExpiration(String jwt) {
        
        return extractAllClaims(jwt).getExpiration();

    }

}
