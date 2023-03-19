package com.weshopify.platform.config;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.weshopify.platform.commands.CreateInvoiceCommand;
import com.weshopify.platform.commands.CreateShippingCommand;
import com.weshopify.platform.events.InvoiceCreatedEvent;
import com.weshopify.platform.events.OrderCreatedEvent;
import com.weshopify.platform.events.OrderShippedEvent;
import com.weshopify.platform.events.PaymentProcessFailedEvent;
import com.weshopify.platform.model.OrderModel;
import com.weshopify.platform.model.OrderStatus;
import com.weshopify.platform.outbound.ProductClient;
import com.weshopify.platform.repo.OrderRepo;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderManagementSaga {

	@Autowired
	private CommandGateway commandBus;
	
	
	  @Autowired 
	  ProductClient productClient;
	 
	
	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
		log.info("Saga Handler Started");
		log.info("Saga invoked ...order created....");
		 String paymentId = UUID.randomUUID().toString();
		CreateInvoiceCommand invoiceCommand = new CreateInvoiceCommand(paymentId, orderCreatedEvent.orderId, orderCreatedEvent.price);
		commandBus.send(invoiceCommand);
		log.info("Dispathed the CreateInvoiceCommand");
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handleInvoiceCreatedEvent(InvoiceCreatedEvent invoiceCreatedEvent) {
		log.info("Saga Handler recived InvoiceCreatedEvent, creating the shipping command");
		 String shippingId = UUID.randomUUID().toString();
		CreateShippingCommand shippingCommand = new CreateShippingCommand(shippingId, 
				invoiceCreatedEvent.orderId, 
				invoiceCreatedEvent.paymentId);
		
		commandBus.send(shippingCommand);
		log.info("Dispathed  CreateShippingCommand");
	}
	
	/**
	 * Compansating Transaction
	 * @param paymentFailedEvent
	 */
	@SagaEventHandler(associationProperty = "orderId")
	public void handlePaymentProcessFailedEvent(PaymentProcessFailedEvent paymentFailedEvent) {
		log.info("Saga Handler recived PaymentProcessFailedEvent");
		
		
		  OrderModel order = OrderRepo.getOrder(paymentFailedEvent.orderId);
		  boolean isRestted = productClient.resetProduct(order.getProductId(),
		  order.getQuantity()); if(isRestted) {
		  order.setStatus(String.valueOf(OrderStatus.REJECTED)); 
		  
		  /**
		   * Note: Here i am updating the Order in the inmem db
		   * you can try with your own db
		   */
		  OrderRepo.updateOrder(order);
		  
		 }
		 
		
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handleOrderShippedEventEvent(OrderShippedEvent orderShippedEvent) {
		log.info("Saga Handler recived OrderShippedEvent. saga ends here and odre db is now updating");
		log.info("updating the order status in the order db");
	}
}
