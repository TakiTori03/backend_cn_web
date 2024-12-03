package com.hust.Ecommerce.services.payment;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CryptoService {

    private final Mac mac = Mac.getInstance("HmacSHA512");

    @Value("${payment.vnPay.secretKey}")
    private String secretKey;

    public CryptoService() throws NoSuchAlgorithmException {
    }

    @PostConstruct
    void init() throws InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
        mac.init(secretKeySpec);
    }

    public String sign(String data) {
        try {
            return toHexString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("error sign payment");
            // throw new BusinessException(ResponseCode.VNPAY_SIGNING_FAILED);
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
