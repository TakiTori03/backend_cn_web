package com.hust.Ecommerce.services.payment;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hust.Ecommerce.constants.VNPayParams;
import com.hust.Ecommerce.dtos.payment.PaymentRequest;
import com.hust.Ecommerce.dtos.payment.PaymentResponse;
import com.hust.Ecommerce.util.DateUtil;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class VnPayServiceImpl implements PaymentService {

    public static final long DEFAULT_MULTIPLIER = 100L;

    public static final String VERSION = "2.1.0";
    public static final String COMMAND = "pay";
    public static final String ORDER_TYPE = "other";

    @Value("${payment.vnPay.tmnCode}")
    private String tmnCode;

    @Value("${payment.vnPay.url}")
    private String initPaymentPrefixUrl;

    @Value("${payment.vnPay.returnUrl}")
    private String returnUrlFormat;

    @Value("${payment.vnPay.timeout}")
    private Integer paymentTimeout;

    private final CryptoService cryptoService;

    @Override
    public PaymentResponse init(PaymentRequest request) {
        var amount = request.getAmount() * DEFAULT_MULTIPLIER; // 1. amount *100
        var txnRef = request.getTxnRef(); // 2. order code
        var returnUrl = buildReturnUrl(txnRef); // 3. FE redirect by returnUrl

        var vnCalendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        var createdDate = DateUtil.formatVnTime(vnCalendar);
        vnCalendar.add(Calendar.MINUTE, paymentTimeout);
        var expiredDate = DateUtil.formatVnTime(vnCalendar); // 4. expiredDate for secure

        var ipAddress = request.getIpAddress();
        var orderInfo = buildPaymentDetail(request);

        Map<String, String> params = new HashMap<>();

        params.put(VNPayParams.VERSION, VERSION);
        params.put(VNPayParams.COMMAND, COMMAND);

        params.put(VNPayParams.TMN_CODE, tmnCode);
        params.put(VNPayParams.AMOUNT, String.valueOf(amount));
        params.put(VNPayParams.CURR_CODE, "VND"); // vnp sandbox hien tai chi ho tro vnd

        params.put(VNPayParams.TXN_REF, txnRef);
        params.put(VNPayParams.RETURN_URL, returnUrl);

        params.put(VNPayParams.CREATE_DATE, createdDate);
        params.put(VNPayParams.EXPIRE_DATE, expiredDate);

        params.put(VNPayParams.IP_ADDR, ipAddress);
        params.put(VNPayParams.LOCALE, "vn");

        params.put(VNPayParams.ORDER_INFO, orderInfo);
        params.put(VNPayParams.ORDER_TYPE, ORDER_TYPE);

        var initPaymentUrl = buildInitPaymentUrl(params);

        return PaymentResponse.builder()
                .vnpUrl(initPaymentUrl)
                .id(txnRef)
                .build();
    }

    private String buildReturnUrl(String txnRef) {
        return String.format(returnUrlFormat, txnRef);
    }

    private String buildPaymentDetail(PaymentRequest request) {
        return String.format("Thanh toan don dat phong %s", request.getTxnRef());
    }

    @SneakyThrows
    private String buildInitPaymentUrl(Map<String, String> params) {
        var hashPayload = new StringBuilder();
        var query = new StringBuilder();
        var fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames); // 1. Sort field names

        var itr = fieldNames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                // 2.1. Build hash data
                hashPayload.append(fieldName);
                hashPayload.append("=");
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                // 2.2. Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    query.append("&");
                    hashPayload.append("&");
                }
            }
        }

        // 3. Build secureHash
        var secureHash = cryptoService.sign(hashPayload.toString());

        // 4. Finalize query
        query.append("&vnp_SecureHash=");
        query.append(secureHash);

        return initPaymentPrefixUrl + "?" + query;
    }
}
