package com.hust.Ecommerce.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.exceptions.AppException;
import com.hust.Ecommerce.exceptions.ErrorCode;
import com.hust.Ecommerce.repositories.authentication.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) {
        log.debug("Authenticating {}", email);

        return userRepository
                .findByEmail(email)
                .map(user -> createSpringSecurityUser(email, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String email, User user) {
        if (user.getStatus() == 0) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVED);
        }

        List<SimpleGrantedAuthority> grantedAuthorities = List
                .of(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                grantedAuthorities);
    }
}
