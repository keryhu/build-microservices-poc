package com.build.warehouse.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.ServiceActivator;

import com.build.event.customer.CustomerEvent;
import com.build.event.order.OrderEvent;
import com.build.warehouse.dao.WarehouseDao;

@EnableBinding(WarehouseChannels.class)
public class WarehouseProcessor {
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@ServiceActivator(inputChannel = WarehouseChannels.CUSTOMER)
	public void handleCustomerEvent(CustomerEvent event) {
		switch (event.getAction()) {
		case DELETE:
			warehouseDao.markCustomerDeleted(event.getId());
			break;
		case SAVE:
			warehouseDao.saveCustomer(event.getEventData());
			break;
		}
	}
	
	@ServiceActivator(inputChannel = WarehouseChannels.ORDER)
	public void handleOrderEvent(OrderEvent event) {
		switch (event.getAction()) {
		case DELETE:
			warehouseDao.markOrderDeleted(event.getId());
			break;
		case SAVE:
			warehouseDao.saveOrder(event.getEventData());
			break;
		}
	}

}
