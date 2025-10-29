package com.rbattezzati.ecommerce.kafka;

import com.rbattezzati.ecommerce.customer.CustomerResponse;
import com.rbattezzati.ecommerce.order.PaymentMethod;
import com.rbattezzati.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customerResponse,
        List<PurchaseResponse> products
) {
}
