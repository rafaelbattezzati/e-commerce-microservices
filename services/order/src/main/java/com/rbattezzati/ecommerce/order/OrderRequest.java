package com.rbattezzati.ecommerce.order;

import com.rbattezzati.ecommerce.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,
        String reference,
        @Positive(message = "Order amount should be positive")
        BigDecimal amount,
        @NotNull(message = "Payment method should be present")
        PaymentMethod paymentMethod,
        @NotNull(message = "Customer Id should be present")
        @NotEmpty(message = "Customer Id should be present")
        @NotBlank(message = "Customer Id should be present")
        String customerId,
        @NotEmpty(message = "At least one product should be present")
        List<PurchaseRequest> products
) {
}
