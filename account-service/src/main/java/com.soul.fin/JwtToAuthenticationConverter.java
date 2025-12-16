package com.soul.fin;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtToAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) roles = List.of();

        List<String> perms = jwt.getClaimAsStringList("permissions");
        if (perms == null) perms = List.of();

        Stream<SimpleGrantedAuthority> roleAuth = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r));

        Stream<SimpleGrantedAuthority> permAuth = perms.stream()
                .map(SimpleGrantedAuthority::new);

        Collection<SimpleGrantedAuthority> authorities =
                Stream.concat(roleAuth, permAuth)
                        .collect(Collectors.toSet());

        return Mono.just(new JwtAuthenticationToken(jwt, authorities));
    }
}
