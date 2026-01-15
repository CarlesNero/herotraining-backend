package com.base.base.login.backend.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuración de seguridad OAuth2 con Keycloak
 * HeroTraining Backend - Adaptado para SSO
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Endpoints de administrador - requieren rol nero-admin
                .requestMatchers("/api/admin/**").hasRole("nero-admin")
                .requestMatchers(HttpMethod.POST, "/api/exercises").hasRole("nero-admin")
                .requestMatchers(HttpMethod.PUT, "/api/exercises/**").hasRole("nero-admin")
                .requestMatchers(HttpMethod.DELETE, "/api/exercises/**").hasRole("nero-admin")
                .requestMatchers(HttpMethod.POST, "/api/achievements").hasRole("nero-admin")
                .requestMatchers(HttpMethod.PUT, "/api/achievements/**").hasRole("nero-admin")
                .requestMatchers(HttpMethod.DELETE, "/api/achievements/**").hasRole("nero-admin")
                
                // Endpoints protegidos - requieren rol default-roles-neroapps
                .requestMatchers("/api/dashboard/**").hasRole("default-roles-neroapps")
                .requestMatchers("/api/workouts/**").hasRole("default-roles-neroapps")
                .requestMatchers("/api/exercises/**").hasRole("default-roles-neroapps")
                .requestMatchers("/api/achievements/**").hasRole("default-roles-neroapps")
                .requestMatchers("/api/**").hasRole("default-roles-neroapps")
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            // Configurar como Resource Server OAuth2
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )
            // Permitir iframes para H2 Console
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            );

        return http.build();
    }

    /**
     * Configuración CORS para permitir peticiones desde frontends
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origenes permitidos (frontends)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",  // Desarrollo local - Angular default
            "http://localhost:4201",  // Desarrollo local - Puerto alternativo
            "https://herotraining.csanchezm.es"  // Producción
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir credenciales (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Tiempo de cache para preflight requests
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Conversor de JWT a authorities de Spring Security
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return jwtAuthenticationConverter;
    }

    /**
     * Conversor personalizado para extraer roles de Keycloak
     */
    static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            // Log del token completo para debug
            System.out.println("=== JWT TOKEN DEBUG ===");
            System.out.println("Token claims: " + jwt.getClaims());
            
            // Extraer realm_access del token
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            System.out.println("Realm access: " + realmAccess);
            
            if (realmAccess == null || realmAccess.isEmpty()) {
                System.out.println("No realm_access found!");
                return List.of();
            }
            
            // Obtener lista de roles
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            System.out.println("Roles found: " + roles);
            
            if (roles == null || roles.isEmpty()) {
                System.out.println("No roles found!");
                return List.of();
            }
            
            // Convertir a GrantedAuthority con prefijo ROLE_
            Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
            
            System.out.println("Authorities created: " + authorities);
            System.out.println("=== END JWT DEBUG ===");
            
            return authorities;
        }
    }
}
