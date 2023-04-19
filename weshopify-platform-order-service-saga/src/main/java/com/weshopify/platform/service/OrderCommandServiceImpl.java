package com.weshopify.platform.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weshopify.platform.bean.OrderBean;
import com.weshopify.platform.commands.CreateOrderCommand;
import com.weshopify.platform.model.OrderModel;
import com.weshopify.platform.model.OrderStatus;
import com.weshopify.platform.outbound.ProductClient;
//import com.weshopify.platform.repo.OrderRepo;
import com.weshopify.platform.repo.OrderRepo;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class OrderCommandServiceImpl implements OrderCommandService {

    private final CommandGateway commandGateway;
    
    @Autowired
    private ProductClient productClient;
    
    //@Autowired
   // private OrderRepo orderRepo;

    public OrderCommandServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> createOrder(OrderBean orderBean) {
    	
    	/**
    	 * step-1: reserving the product 
    	 */
    	boolean isReserved = productClient.reservProduct(orderBean.getProductId(), orderBean.getQuantity());
    	log.info("is product reserved?:\t"+isReserved); 
    	String orderId = UUID.randomUUID().toString();
    	if(isReserved) {
    		OrderModel orderModel = new OrderModel();
    		BeanUtils.copyProperties(orderBean, orderModel);
    		orderModel.setStatus(String.valueOf(OrderStatus.CREATED));
    		//save order in db
    		orderModel.setOrderId(orderId);
    		
    		/**
    		 * placing the order, if order will fail then 
    		 * product stock should reset
    		 * Note: For Now i am storing in inmem cache, 
    		 * but you can try out the read db
    		 */
    		OrderRepo.putOrder(orderModel);
    		
    		return commandGateway.send(new CreateOrderCommand(orderId, orderBean.getItemType(),
	        		orderBean.getPrice(), orderBean.getCurrency(), String.valueOf(OrderStatus.CREATED)));
    		 
    	}else {
    		 return commandGateway.send(new CreateOrderCommand(orderId, orderBean.getItemType(),
    	        		orderBean.getPrice(), orderBean.getCurrency(), String.valueOf(OrderStatus.REJECTED)));
    	}
    	
       
    }
}
