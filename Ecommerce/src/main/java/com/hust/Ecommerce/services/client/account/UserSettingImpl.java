package com.hust.Ecommerce.services.client.account;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.Ecommerce.entities.authentication.GenderEnum;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.exceptions.AppException;
import com.hust.Ecommerce.exceptions.ErrorCode;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.security.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserSettingImpl implements UserSetting {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updatePersonalSetting(String name, GenderEnum gender, String phoneNumber, String address, String avatar,
            Instant datOfBirth) {
        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findByEmail)
                .ifPresent(user -> {
                    user.setName(name);
                    user.setPhoneNumber(phoneNumber);
                    user.setGender(gender);
                    user.setAddress(address);
                    user.setAvatar(avatar);
                    user.setDateOfBirth(datOfBirth);
                    // save update user
                    userRepository.save(user);
                    log.debug("Changed Information for User: {}", user);
                });
    }

    public void changePassword(String currentPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findByEmail)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentPassword, currentEncryptedPassword)) {
                        throw new AppException(ErrorCode.INVALID_PASSWORD);
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    log.debug("Changed password for User: {}", user);
                });
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
