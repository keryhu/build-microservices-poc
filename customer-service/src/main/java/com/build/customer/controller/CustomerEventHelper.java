package com.build.customer.controller;

import com.build.customer.domain.Customer;
import com.build.event.customer.CustomerEvent;
import com.build.event.customer.CustomerEventData;

public class CustomerEventHelper {
	
	public static CustomerEvent createSaveEvent(Customer customer) {
		CustomerEventData eventData = new CustomerEventData();
		eventData.setId(customer.getId());
		eventData.setEmail(customer.getEmail());
		eventData.setName(customer.getName());
		return CustomerEvent.save(eventData);
	}

}
