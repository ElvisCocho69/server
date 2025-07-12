package com.api.server.config.authorization;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.security.Operation;
import com.api.server.persistence.entity.security.User;
import com.api.server.persistence.repository.security.OperationRepository;
import com.api.server.service.security.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
            RequestAuthorizationContext requestContext) {

        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);

        if (isPublic) {
            return new AuthorizationDecision(true);
        }

        boolean isGranted = isGranted(url, httpMethod, authentication.get());

        return new AuthorizationDecision(isGranted);

    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {

        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<Operation> operations = obtainedOperations(authentication);

        boolean isGranted = operations.stream().anyMatch(extracted(url, httpMethod));

        return isGranted;

    }

    private Predicate<? super Operation> extracted(String url, String httpMethod) {
        return operation -> {
            String basePath = operation.getModule().getBasePath();
            String fullPattern = basePath.concat(operation.getPath());
            
            // Convert Spring path patterns to regex patterns
            String regexPattern = fullPattern
                .replace("{", "(?<")  // Convert {param} to (?<param>
                .replace("}", ">[^/]+)"); // Add regex for parameter value
            
            System.out.println("Checking URL: " + url + " against pattern: " + fullPattern + " | Method: " + httpMethod);
            
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(url);
            
            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }
    

    private List<Operation> obtainedOperations(Authentication authentication) {

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = (String) authToken.getPrincipal();
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        return user.getRole().getPermissions().stream()
            .map(grantedPermission -> grantedPermission.getOperation())
            .collect(Collectors.toList());

    }

    private boolean isPublic(String url, String httpMethod) {

        List<Operation> publicAccessEndpoints = operationRepository.findByPublicAccess();

        boolean isPublic = publicAccessEndpoints.stream().anyMatch(extracted(url, httpMethod));

        System.out.println("IS PUBLIC: " + isPublic);

        return isPublic;

    }

    private String extractUrl(HttpServletRequest request) {

        String contextPath = request.getContextPath();
        String url = request.getRequestURI();
        url = url.replace(contextPath, "");
        System.out.println(url);

        return url;

    }

}
