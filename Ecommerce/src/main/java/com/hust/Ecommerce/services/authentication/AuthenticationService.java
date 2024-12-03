package com.hust.Ecommerce.services.authentication;

import java.util.Optional;

import com.hust.Ecommerce.dtos.authentication.AdminUserDTO;
import com.hust.Ecommerce.entities.authentication.Token;
import com.hust.Ecommerce.entities.authentication.User;

public interface AuthenticationService {
    public User registerUser(AdminUserDTO userDTO, String password);

    public Optional<User> activateRegistration(String key);

    public Token login(String email, String password);

    public Token refreshToken(String refreshToken);

    public Optional<User> forgetPassword(String email);

    public Optional<User> resetPassword(String newPassword, String key);

    public void logout(String jwt);

    public Optional<User> getUserWithAuthorities();
}
