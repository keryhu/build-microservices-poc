package com.build.order.controller;

import com.build.event.order.OrderEvent;
import com.build.event.order.OrderEventData;
import com.build.order.domain.Order;

public class OrderEventHelper {
	
	public static OrderEvent createSaveEvent(Order order) {
		OrderEventData orderEventData = new OrderEventData();
		orderEventData.setId(order.getId());
		orderEventData.setCustomerId(order.getCustomerId());
		orderEventData.setLineItem(order.getLineItem());
		orderEventData.setQuantity(order.getQuantity());
		orderEventData.setUnitPrice(order.getUnitPrice());
		return OrderEvent.save(orderEventData);
	}

}
