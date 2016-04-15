package com.build.order.dao;

import java.util.List;

import com.build.order.domain.Order;

public interface OrderDao {

	Order getOrder(int id);
	
	void insertOrder(Order order);
	
	void updateOrder(Order order);
	
	void deleteOrder(int id);
	
	List<Order> getCustomerOrders(int customerId);
}
