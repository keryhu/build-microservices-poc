package com.build.event.customer;

import com.build.event.CommandAction;

public class CustomerEvent {

	private CommandAction action;

	private CustomerEventData eventData;

	private Integer id;
	
	public static CustomerEvent save(CustomerEventData customer) {
		return new CustomerEvent(CommandAction.SAVE, customer, null);
	}
	
	public static CustomerEvent delete(int id) {
		return new CustomerEvent(CommandAction.DELETE, null, id);
	}
	
	public CustomerEvent() {
	}
	
	private CustomerEvent(CommandAction action, CustomerEventData eventData, Integer id) {
		this.action = action;
		this.eventData = eventData;
		this.id = id;
	}

	public CommandAction getAction() {
		return action;
	}

	public void setAction(CommandAction action) {
		this.action = action;
	}
	
	public CustomerEventData getEventData() {
		return eventData;
	}
	
	public void setEventData(CustomerEventData eventData) {
		this.eventData = eventData;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
