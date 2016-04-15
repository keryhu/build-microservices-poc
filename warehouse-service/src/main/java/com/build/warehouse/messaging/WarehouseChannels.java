package com.build.warehouse.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface WarehouseChannels {
	
	String CUSTOMER = "customer";
	
	String ORDER = "order";

	@Input(CUSTOMER)
	SubscribableChannel customer();
	
	@Input(ORDER)
	SubscribableChannel order();
}
