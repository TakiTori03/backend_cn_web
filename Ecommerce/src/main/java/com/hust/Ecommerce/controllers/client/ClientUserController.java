package com.hust.Ecommerce.controllers.client;

import java.util.List;
import java.util.Optional;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.constants.MessageKeys;

import com.hust.Ecommerce.dtos.ApiResponse;

import com.hust.Ecommerce.dtos.authentication.AdminUserDTO;
import com.hust.Ecommerce.dtos.authentication.PasswordChangeDTO;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.exceptions.AppException;
import com.hust.Ecommerce.exceptions.ErrorCode;
import com.hust.Ecommerce.security.SecurityUtils;

import com.hust.Ecommerce.services.client.account.UserSetting;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/client-api/users")
@AllArgsConstructor
public class ClientUserController {
        private UserSetting userSetting;

        @GetMapping("/info")
        public ResponseEntity<ApiResponse<?>> getUserInfo() {

                AdminUserDTO userResponse = userSetting
                                .getUserWithAuthorities()
                                .map(AdminUserDTO::new)
                                .orElseThrow(() -> new UsernameNotFoundException(MessageKeys.USER_NOT_FOUND));

                return ResponseEntity.ok(ApiResponse.<AdminUserDTO>builder()
                                .success(true)
                                .payload(userResponse)
                                .build());

        }

        @PutMapping("/personal")
        public ResponseEntity<ApiResponse<?>> updatePersonalSetting(@Valid @RequestBody AdminUserDTO userDTO) {

                String email = SecurityUtils.getCurrentUserLogin()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

                User user = userSetting.getUserWithAuthoritiesByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));

                userSetting.updatePersonalSetting(
                                userDTO.getName(),
                                userDTO.getGender(),
                                userDTO.getPhoneNumber(),
                                userDTO.getAddress(),
                                userDTO.getAvatar(),
                                userDTO.getDateOfBirth());

                return ResponseEntity.ok(ApiResponse.<AdminUserDTO>builder()
                                .success(true)
                                .payload(new AdminUserDTO(user))
                                .build());

        }

        @PutMapping("/password")
        public ResponseEntity<ApiResponse<?>> updatePasswordSetting(@RequestBody PasswordChangeDTO passwordChangeDto,
                        BindingResult bindingResult) {

                if (bindingResult.hasErrors()) {
                        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                        return ResponseEntity.badRequest().body(
                                        ApiResponse.builder()
                                                        .message(MessageKeys.ERROR_MESSAGE)
                                                        .errors(errorMessages).build());
                }
                userSetting.changePassword(passwordChangeDto.getCurrentPassword(),
                                passwordChangeDto.getNewPassword());
                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .build());

        }

}
