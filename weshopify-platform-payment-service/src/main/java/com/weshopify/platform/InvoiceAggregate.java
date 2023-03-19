package com.weshopify.platform;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.weshopify.platform.commands.CreateInvoiceCommand;
import com.weshopify.platform.events.InvoiceCreatedEvent;
import com.weshopify.platform.events.PaymentProcessFailedEvent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
@Data
public class InvoiceAggregate {

	@AggregateIdentifier
    private String paymentId;

    private String orderId;
    
   private BigDecimal price;
   
   private String status;
   
   @CommandHandler
   public InvoiceAggregate(CreateInvoiceCommand createInvoiceCommand) {
	   log.info("i am  in Invoice Aggregate Command Handler , recived the CreateInvoiceCommand");
	   log.info("creating the CreateInvoiceCommand");
	   if(createInvoiceCommand.price != null) {
		   if(createInvoiceCommand.price.doubleValue() != 0) {
			   AggregateLifecycle.apply(new InvoiceCreatedEvent(createInvoiceCommand.paymentId, createInvoiceCommand.orderId));
			   log.info("disptched the CreateInvoiceCommand");
		   }else {
			   log.info("Invoice Not creating due to payment not reflected");
			   AggregateLifecycle.apply(new PaymentProcessFailedEvent(createInvoiceCommand.paymentId, createInvoiceCommand.orderId));
			   log.info("disptched the PaymentProcessFailedEvent");
		   }
	   }else {
		   log.info("Invoice Not creating due to payment failures");
		   AggregateLifecycle.apply(new PaymentProcessFailedEvent(createInvoiceCommand.paymentId, createInvoiceCommand.orderId));
		   log.info("disptched the PaymentProcessFailedEvent");
	   }
	  
   }
   
   @EventSourcingHandler
   public void paymentProcessFailedEventSourcingHandler(PaymentProcessFailedEvent paymentFailedEvent) {
	   log.info("failed payment event published");
	   this.orderId = paymentFailedEvent.orderId;
	   this.paymentId = paymentFailedEvent.paymentId;
	   this.status = InvoiceStatus.PAYMENT_REVERSED.name();
   }
   
   @EventSourcingHandler
   public void invoiceCreatedEventSourcingHandler(InvoiceCreatedEvent invoiceEvent) {
	   log.info("invoice created event published");
	   this.orderId = invoiceEvent.orderId;
	   this.paymentId = invoiceEvent.paymentId;
	   this.status = InvoiceStatus.PAID.name();
   }
}
