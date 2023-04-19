package com.weshopify.platform.service;

import java.util.concurrent.CompletableFuture;

import com.weshopify.platform.bean.OrderBean;


public interface OrderCommandService {

    public CompletableFuture<String> createOrder(OrderBean orderBean);

}
