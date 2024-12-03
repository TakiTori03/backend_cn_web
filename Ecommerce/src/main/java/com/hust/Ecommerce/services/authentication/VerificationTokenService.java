package com.hust.Ecommerce.services.authentication;

import com.hust.Ecommerce.entities.authentication.Token;
import com.hust.Ecommerce.entities.authentication.User;

public interface VerificationTokenService {
    public Token addTokenEndRefreshToken(User user, String token);

    public Token verifyRefreshToken(String refreshToken);

    public void deleteTokenWithToken(Token token);

    public void deleteTokenWithJwt(String jwt);

}
