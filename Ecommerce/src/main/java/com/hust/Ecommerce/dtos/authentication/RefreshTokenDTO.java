package com.hust.Ecommerce.dtos.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
