package com.hust.Ecommerce.controllers.authentication;

import java.io.IOException;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.constants.FieldName;
import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.authentication.AdminUserDTO;
import com.hust.Ecommerce.dtos.authentication.EmailInput;
import com.hust.Ecommerce.dtos.authentication.ForgotPasswordDTO;
import com.hust.Ecommerce.dtos.authentication.LoginResponse;
import com.hust.Ecommerce.dtos.authentication.LoginVM;
import com.hust.Ecommerce.dtos.authentication.ManagedUserVM;
import com.hust.Ecommerce.dtos.authentication.RefreshTokenDTO;
import com.hust.Ecommerce.entities.authentication.Token;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.exceptions.AppException;
import com.hust.Ecommerce.exceptions.ErrorCode;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.security.SecurityUtils;
import com.hust.Ecommerce.services.authentication.AuthenticationService;
import com.hust.Ecommerce.services.mail.MailServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

        private final AuthenticationService authenticationService;
        private final MailServiceImpl mailService;

        @PostMapping("/registration")
        public ResponseEntity<ApiResponse<?>> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM,
                        BindingResult bindingResult) {

                if (bindingResult.hasErrors()) {
                        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                        return ResponseEntity.badRequest().body(
                                        ApiResponse.builder()
                                                        .message(MessageKeys.ERROR_MESSAGE)
                                                        .errors(errorMessages).build());
                }
                User user = authenticationService.registerUser(managedUserVM, managedUserVM.getPassword());
                mailService.sendActivationEmail(user);

                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message(MessageKeys.REGISTER_ACCOUNT_SUCCESS)
                                .build());

        }

        @GetMapping("/registration/confirm")
        public void activateAccount(@RequestParam(value = "key") String key, HttpServletResponse response)
                        throws IOException {

                authenticationService.activateRegistration(key)
                                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

                // Redirect to the login page on the frontend
                response.sendRedirect(AppConstants.FRONTEND_HOST + "/login");
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginVM loginVM,
                        BindingResult bindingResult) {

                if (bindingResult.hasErrors()) {
                        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                        return ResponseEntity.badRequest().body(
                                        ApiResponse.builder()
                                                        .message(MessageKeys.LOGIN_FAILED)
                                                        .errors(errorMessages)
                                                        .build());
                }

                Token token = authenticationService.login(loginVM.getEmail(), loginVM.getPassword());

                AdminUserDTO userResponse = new AdminUserDTO(token.getUser());

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setBearerAuth(token.getToken());

                return new ResponseEntity<>(ApiResponse.builder()
                                .success(true)
                                .payload(LoginResponse.builder()
                                                .token(token.getToken())
                                                .refreshToken(token.getRefreshToken())
                                                .user(userResponse)
                                                .build())
                                .build(), httpHeaders, HttpStatus.OK);

        }

        @GetMapping("/info")
        public ResponseEntity<ApiResponse<?>> getAdminUserInfo() {

                AdminUserDTO userResponse = authenticationService
                                .getUserWithAuthorities()
                                .map(AdminUserDTO::new)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .payload(userResponse)
                                .build());

        }

        // refreshToken
        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponse<?>> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {

                Token newToken = authenticationService.refreshToken(refreshTokenDTO.getRefreshToken());
                AdminUserDTO userResponse = new AdminUserDTO(newToken.getUser());

                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .payload(LoginResponse.builder()
                                                .token(newToken.getToken())
                                                .refreshToken(newToken.getRefreshToken())
                                                .user(userResponse)
                                                .build())
                                .build());

        }

        @PostMapping(path = "/forgot-password")
        public void forgotPassword(@RequestBody EmailInput email) {
                User user = authenticationService.forgetPassword(email.getEmail()).orElseThrow(
                                () -> new ResourceNotFoundException(ResourceName.USER, FieldName.EMAIL, email));

                mailService.sendPasswordResetMail(user);

        }

        @PutMapping(path = "/reset-password")
        public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO,
                        @RequestParam(value = "key") String key,
                        BindingResult bindingResult) {

                if (bindingResult.hasErrors()) {
                        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                        return ResponseEntity.badRequest().body(
                                        ApiResponse.builder()
                                                        .message(MessageKeys.ERROR_MESSAGE)
                                                        .errors(errorMessages).build());
                }
                authenticationService.resetPassword(forgotPasswordDTO.getPassword(),
                                key).orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));

                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true).build());

        }

        @GetMapping("/logout")
        public ResponseEntity<ApiResponse<?>> logout() {

                String jwt = SecurityUtils.getCurrentUserJWT()
                                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.ACCOUNT_NOT_LOGIN));

                authenticationService.logout(jwt);
                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .build());

        }
}
