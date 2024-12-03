package com.hust.Ecommerce.services.authentication;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.Ecommerce.components.JwtTokenUtil;
import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.constants.RoleKeys;
import com.hust.Ecommerce.dtos.authentication.AdminUserDTO;
import com.hust.Ecommerce.entities.authentication.Role;
import com.hust.Ecommerce.entities.authentication.Token;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.exceptions.AppException;
import com.hust.Ecommerce.exceptions.ErrorCode;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.repositories.authentication.RoleRepository;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.security.SecurityUtils;
import com.hust.Ecommerce.util.RandomUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final VerificationTokenServiceImpl verificationTokenService;

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
                .findByEmail(userDTO.getEmail())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new AppException(ErrorCode.USER_EXISTED);
                    }
                });

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);

        newUser.setEmail(userDTO.getEmail());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);

        if (userDTO.getName() != null) {
            newUser.setName(userDTO.getName());
        }
        if (userDTO.getPhoneNumber() != null) {
            newUser.setPhoneNumber(userDTO.getPhoneNumber());
        }
        // config image url
        if (userDTO.getAvatar() != null) {
            newUser.setAvatar(userDTO.getAvatar());
        }
        if (userDTO.getLanguage() != null) {
            newUser.setLanguage(userDTO.getLanguage());
        } else {
            newUser.setLanguage(AppConstants.DEFAULT_LANGUAGE);
        }
        // new user is not active
        newUser.setStatus(0);

        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());

        // Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findById(RoleKeys.USER)
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.ROLE_NOT_FOUND));

        newUser.setRole(role);
        userRepository.save(newUser);

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
                .findByActivationKey(key)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setStatus(1);
                    user.setActivationKey(null);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getStatus() != 0) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    public Token login(String email, String password) {
        return createTokenAndSave(email, password);
    }

    public Token refreshToken(String refreshToken) {
        // xac thuc refresh token dung 0
        Token token = verificationTokenService.verifyRefreshToken(refreshToken);

        // lay user
        User user = token.getUser();

        // xoa token cu
        verificationTokenService.deleteTokenWithToken(token);

        // tao token moi cung voi refresh token moi
        Token newToken = createTokenAndSaveForRefresh(user);

        return newToken;
    }

    public Optional<User> forgetPassword(String email) {
        return userRepository
                .findByEmail(email)
                .filter(user -> user.getStatus() != 0)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    return user;
                });
    };

    public Optional<User> resetPassword(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
                .findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    return user;
                });
    }

    public void logout(String jwt) {
        verificationTokenService.deleteTokenWithJwt(jwt);
    }

    // kiem tra exist email, authenticated , create accesstoken, refreshtoken, save
    public Token createTokenAndSave(String email, String password) {

        // exists by user
        User user = getUserWithAuthoritiesByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));

        String token;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                    password);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.createJwt(authentication);
        } catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.EMAIL_PASSWORD_NOT_MATCH);
        }

        return verificationTokenService.addTokenEndRefreshToken(user, token);
    }

    public Token createTokenAndSaveForRefresh(User user) {
        String token = jwtTokenUtil.createJwtFromUser(user);
        return verificationTokenService.addTokenEndRefreshToken(user, token);

    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findByEmail);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
