package com.rbattezzati.ecommerce.order;

import com.rbattezzati.ecommerce.customer.CustomerClient;
import com.rbattezzati.ecommerce.exception.BusinessException;
import com.rbattezzati.ecommerce.product.ProductClient;
import com.rbattezzati.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;

    public Integer createOrder(OrderRequest request){
        //check the customer -> OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("No customer exists"));

        //purchase products -> product-service
        this.productClient.purchaseProducts(request.products());

        //persist order
        var order = this.repository.save(mapper.toOrder(request));

        //persist orderlines
        for(PurchaseRequest)

        //start payment process

        //send the order confirmation  --> notification-service


    }
}
