package com.api.server.config.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.security.JwtToken;
import com.api.server.persistence.entity.security.User;
import com.api.server.persistence.repository.security.JwtTokenRepository;
import com.api.server.service.auth.JwtService;
import com.api.server.service.security.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepository jwtRepository;

    @Autowired
    private UserService userService;

    /** Para evaluar rutas tipo "/files/**" */
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        /* -----------------------------------------------------------
           BYPASS para archivos públicos: /files/**  (GET y HEAD)
           ----------------------------------------------------------- */
        String path   = request.getServletPath();
        String method = request.getMethod();

        if (pathMatcher.match("/files/**", path) &&
            (HttpMethod.GET.matches(method) || HttpMethod.HEAD.matches(method))) {

            // Pasa la petición sin añadir autenticación ni tocar el contexto
            filterChain.doFilter(request, response);
            return;
        }

        /* -----------------------------------------------------------
           Resto de lógica JWT normal
           ----------------------------------------------------------- */
        String jwt = jwtService.extractJwtFromRequest(request);

        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener token registrado y comprobar validez
        Optional<JwtToken> tokenOpt = jwtRepository.findByToken(jwt);
        if (!validateToken(tokenOpt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer usuario y poblar SecurityContext
        String username = jwtService.extractUsername(jwt);

        User user = userService.findOneByUsername(username)
                .orElseThrow(() ->
                     new ObjectNotFoundException("User not found. Username: " + username));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Continuar con el resto de filtros
        filterChain.doFilter(request, response);
    }

    /* -----------------------------------------------------------
       Métodos auxiliares
       ----------------------------------------------------------- */

    private boolean validateToken(Optional<JwtToken> optionalJwtToken) {

        if (optionalJwtToken.isEmpty()) {
            System.out.println("Token no existe o no fue emitido por nuestro sistema");
            return false;
        }

        JwtToken token = optionalJwtToken.get();
        boolean valid = token.isValid() && token.getExpiration().after(new Date());

        if (!valid) {
            System.out.println("Token inválido o expirado");
            token.setValid(false);
            jwtRepository.save(token);
        }
        return valid;
    }
}
