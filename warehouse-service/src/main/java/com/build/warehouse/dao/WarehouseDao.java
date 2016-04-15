package com.build.warehouse.dao;

import com.build.event.customer.CustomerEventData;
import com.build.event.order.OrderEventData;

public interface WarehouseDao {

	void markCustomerDeleted(int customerId);
	void saveCustomer(CustomerEventData customer);
	
	void markOrderDeleted(int orderId);
	void saveOrder(OrderEventData order);
}
