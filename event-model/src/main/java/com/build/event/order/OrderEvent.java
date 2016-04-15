package com.build.event.order;

import com.build.event.CommandAction;

public class OrderEvent {

	private CommandAction action;

	private OrderEventData eventData;

	private Integer id;
	
	public static OrderEvent save(OrderEventData eventData) {
		return new OrderEvent(CommandAction.SAVE, eventData, null);
	}
	
	public static OrderEvent delete(int id) {
		return new OrderEvent(CommandAction.DELETE, null, id);
	}
	
	public OrderEvent() {
	}
	
	private OrderEvent(CommandAction action, OrderEventData eventData, Integer id) {
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
	
	public OrderEventData getEventData() {
		return eventData;
	}
	
	public void setEventData(OrderEventData eventData) {
		this.eventData = eventData;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
