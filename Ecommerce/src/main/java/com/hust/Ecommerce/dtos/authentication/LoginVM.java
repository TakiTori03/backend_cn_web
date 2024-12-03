package com.hust.Ecommerce.dtos.authentication;

import com.hust.Ecommerce.constants.MessageKeys;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginVM {
    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @NotNull
    @NotBlank(message = MessageKeys.EMAIL_REQUIRED)
    private String email;

    @NotNull

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = MessageKeys.PASSWORD_REQUIRED)
    @NotBlank(message = MessageKeys.PASSWORD_REQUIRED)
    private String password;
}
