package com.build.coordination.domain;

import java.util.List;

public class CustomerOrderView {

	private Customer customer;
	private List<Order> orders;
	
	public CustomerOrderView() {
		
	}
	
	public CustomerOrderView(Customer customer, List<Order> orders) {
		this.customer = customer;
		this.orders = orders;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
