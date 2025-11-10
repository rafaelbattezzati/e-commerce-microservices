package com.rbattezzati.ecommerce.payment;

import com.rbattezzati.ecommerce.customer.CustomerResponse;
import com.rbattezzati.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer) {
}
