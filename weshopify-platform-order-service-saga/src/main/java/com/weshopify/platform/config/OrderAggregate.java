package com.weshopify.platform.config;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.weshopify.platform.commands.CreateOrderCommand;
import com.weshopify.platform.events.OrderCreatedEvent;
import com.weshopify.platform.model.OrderStatus;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class OrderAggregate {

	@AggregateIdentifier
	public String orderId;

    public String itemType;

    public BigDecimal price;

    public String currency;

    public String orderStatus;
    
    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
    	log.info("Order Aggregate recived the create Order Command");
    	log.info("Command is now converting to event");
    	
    	OrderCreatedEvent event = new OrderCreatedEvent(command.orderId, 
    			command.itemType, 
    			command.price, 
    			command.currency,
    			OrderStatus.CREATED.name());
    	log.info("publishing the order created event");
    	AggregateLifecycle.apply(event);    	
    }
    
    @EventSourcingHandler
    public void OrderCreatedEventHandler(OrderCreatedEvent orderEvent) {
    	log.info("order created event published");
    	this.orderId = orderEvent.orderId;
    	this.itemType = orderEvent.itemType;
    	this.currency = orderEvent.currency;
    	this.orderStatus = orderEvent.orderStatus;
    	this.price = orderEvent.price;
    }
}
