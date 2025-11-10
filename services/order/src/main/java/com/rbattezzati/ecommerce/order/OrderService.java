package com.rbattezzati.ecommerce.order;

import com.rbattezzati.ecommerce.customer.CustomerClient;
import com.rbattezzati.ecommerce.exception.BusinessException;
import com.rbattezzati.ecommerce.kafka.OrderConfirmation;
import com.rbattezzati.ecommerce.kafka.OrderProducer;
import com.rbattezzati.ecommerce.orderline.OrderLineRequest;
import com.rbattezzati.ecommerce.orderline.OrderLineService;
import com.rbattezzati.ecommerce.payment.PaymentClient;
import com.rbattezzati.ecommerce.payment.PaymentRequest;
import com.rbattezzati.ecommerce.product.ProductClient;
import com.rbattezzati.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request){
        //check the customer -> OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("No customer exists"));

        //purchase products -> product-service
        var purchaseProducts = this.productClient.purchaseProducts(request.products());

        //persist order
        var order = this.repository.save(mapper.toOrder(request));

        //persist orderlines
        for(PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        //send the order confirmation  --> notification-service (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                    request.reference(),
                    request.amount(),
                    request.paymentMethod(),
                    customer,
                    purchaseProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId){
        return repository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("No order found"));
    }
}
