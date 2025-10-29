package com.rbattezzati.ecommerce.orderline;

public record OrderLineResponse(
        Integer orderId,
        double quantity
) {
}
