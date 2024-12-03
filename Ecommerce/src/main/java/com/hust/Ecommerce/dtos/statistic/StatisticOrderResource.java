package com.hust.Ecommerce.dtos.statistic;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticOrderResource {
    private Instant date;
    private Long totalOrder;
    // tong so tien truoc thue, shipping cost
    private Long totalAmount;
    // tong so tien sau thue, shipping cost ma user phai tra
    private Long totalPay;
    // so san pham ban ra
    private Integer quantity;
}
