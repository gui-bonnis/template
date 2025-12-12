package com.soul.fin.common.api.rest.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                  Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtConverter) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(ex -> ex
                        // optional route-level protections; method-level will do the heavy lifting
                        .pathMatchers("/public/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
                )
                .build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> Mono.just(convert(jwt));
    }

    private AbstractAuthenticationToken convert(Jwt jwt) {
        // extract roles and permissions
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) roles = List.of();

        List<String> perms = jwt.getClaimAsStringList("permissions");
        if (perms == null) perms = List.of();

        // Convert to GrantedAuthority
        Stream<SimpleGrantedAuthority> roleAuth = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r)); // if you use hasRole anywhere

        Stream<SimpleGrantedAuthority> permAuth = perms.stream()
                .map(SimpleGrantedAuthority::new); // plain authority, e.g. "order.create"

        Collection<SimpleGrantedAuthority> authorities =
                Stream.concat(roleAuth, permAuth)
                        .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities);
    }
}

