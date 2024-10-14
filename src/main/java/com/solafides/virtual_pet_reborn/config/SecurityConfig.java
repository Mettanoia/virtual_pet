package com.solafides.virtual_pet_reborn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.HttpStatus.FOUND;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(formLoginSpec -> formLoginSpec

                        // Using default /login and /logout pages

                        .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                            String username = authentication.getName();
                            // Set HTTP status 302 (Found) for redirect and the location header
                            webFilterExchange.getExchange().getResponse().setStatusCode(FOUND);
                            webFilterExchange.getExchange().getResponse().getHeaders().setLocation(URI.create("/index.html?username=" + username));
                            return webFilterExchange.getExchange().getResponse().setComplete();
                        })


                        .authenticationFailureHandler((webFilterExchange, exception) ->
                                webFilterExchange.getExchange().getResponse().setComplete())
                )

                .authorizeExchange(exchanges -> exchanges

                        // Allow access to index.html and static resources for all users
                        .pathMatchers("/", "/index.html", "/css/**", "/images/**", "/js/**").permitAll()
                        // Allow access to the login page for all users
                        .pathMatchers("/login").permitAll()
                        // Allow regular users to access their own resources based on username in the URL
                        .pathMatchers("/api/pets/{username}/{id}", "/api/pets/{username}", "/api/pets").access(this::userMatchesPathOrAdmin)
                        // All other requests must be authenticated
                        .anyExchange().authenticated()
                )
                .build();
    }

    private Mono<AuthorizationDecision> userMatchesPathOrAdmin(Mono<Authentication> authenticationMono, AuthorizationContext context) {

        return authenticationMono.map(auth -> {

            // Check if the user is an admin
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin)
                // Admin can access any resource
                return new AuthorizationDecision(true);

            // Extract the {username} part from the URL
            String path = context.getExchange().getRequest().getURI().getPath();
            String[] pathSegments = path.split("/");

            // Ensure the path has the expected format
            if (pathSegments.length >= 3) {
                String usernameInPath = pathSegments[3];  // {username} is the 3rd part of the URL

                String principalName = auth.getName();    // The authenticated user's username

                // Allow access only if the authenticated user matches the {username} in the URL
                return new AuthorizationDecision(principalName.equals(usernameInPath));
            }

            // Deny access if the URL does not match the expected pattern
            return new AuthorizationDecision(false);
        });

    }

}
