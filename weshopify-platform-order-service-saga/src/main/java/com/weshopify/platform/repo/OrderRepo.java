package com.weshopify.platform.repo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.weshopify.platform.model.OrderModel;

public interface OrderRepo {

	Map<String, OrderModel> map = new LinkedHashMap<>();
	
	public static void putOrder(OrderModel orderModel) {
		map.put(orderModel.getOrderId(), orderModel);
	}
	
	public static OrderModel getOrder(String orderId) {
		return map.get(orderId);
	}
	
	public static OrderModel updateOrder(OrderModel updatedOrder) {
		map.remove(updatedOrder.getOrderId());
		return map.put(updatedOrder.getOrderId(), updatedOrder);
	}
}
