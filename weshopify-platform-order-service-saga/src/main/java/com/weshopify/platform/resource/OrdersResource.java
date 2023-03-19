package com.weshopify.platform.resource;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weshopify.platform.bean.OrderBean;
import com.weshopify.platform.service.OrderCommandService;

@RestController
@RequestMapping(value = "/api/orders")
public class OrdersResource {

    private OrderCommandService orderCommandService;

    public OrdersResource(OrderCommandService orderCommandService) {
        this.orderCommandService = orderCommandService;
    }

    @PostMapping
    public CompletableFuture<String> createOrder(@RequestBody OrderBean orderBean){
        return orderCommandService.createOrder(orderBean);
    }
}
