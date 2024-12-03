package com.hust.Ecommerce.components;

import static com.hust.Ecommerce.security.SecurityUtils.AUTHORITIES_KEY;
import static com.hust.Ecommerce.security.SecurityUtils.JWT_ALGORITHM;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.hust.Ecommerce.entities.authentication.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration-token}")
    private long tokenValidityInSeconds;
    private final JwtEncoder jwtEncoder;

    public String createJwt(Authentication authentication) {

        try {
            String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            Instant now = Instant.now();
            Instant validity;

            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        
    }

    public String createJwtFromUser(User user){
        try {
            String authorities = user.getRole().getName();

            Instant now = Instant.now();
            Instant validity;

            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(user.getEmail())
            .claim(AUTHORITIES_KEY, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
