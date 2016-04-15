package com.build.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.event.order.OrderEvent;
import com.build.order.dao.OrderDao;
import com.build.order.domain.Order;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private Source source;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Order getOrder(@PathVariable("id") int id) {
		return orderDao.getOrder(id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Order> getCustomerOrders(@RequestParam("customerId") int customerId) {
		return orderDao.getCustomerOrders(customerId);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Order insertOrder(@RequestBody Order order) {
		orderDao.insertOrder(order);
		com.build.event.order.OrderEventData eventOrder = new com.build.event.order.OrderEventData();
		eventOrder.setCustomerId(order.getCustomerId());
		source.output().send(MessageBuilder.withPayload(OrderEventHelper.createSaveEvent(order)).build());
		return order;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void updateOrder(@PathVariable("id") int id, @RequestBody Order order) {
		order.setId(id);
		orderDao.updateOrder(order);
		source.output().send(MessageBuilder.withPayload(OrderEventHelper.createSaveEvent(order)).build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteOrder(@PathVariable("id") int id) {
		orderDao.deleteOrder(id);
		source.output().send(MessageBuilder.withPayload(OrderEvent.delete(id)).build());
	}
}
