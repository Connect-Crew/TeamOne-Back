package com.connectcrew.teamone.compositeservice.auth.adapter.in;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtProvider jwtProvider;

    private final JwtAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Save method not supported");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        try {
            String token = jwtProvider.resolveToken(swe.getRequest());
            if (token != null) {
                Authentication auth = jwtProvider.getAuthentication(token);

                return authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
            } else {
                return Mono.empty();
            }
        } catch (UnauthorizedException e) {
            return Mono.empty();
        }
    }
}