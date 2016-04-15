package com.build.customer.dao;

import java.util.List;

import com.build.customer.domain.Customer;

public interface CustomerDao {

	Customer getCustomer(int id);
	
	List<Customer> getCustomersByName(String name);
	
	void insertCustomer(Customer customer);
	
	void updateCustomer(Customer customer);
	
	void deleteCustomer(int id);
}
