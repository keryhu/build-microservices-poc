package com.build.coordination.customer;

import java.util.List;

import com.build.coordination.domain.Customer;
import com.build.coordination.domain.CustomerOrderView;

import rx.Single;

public interface CustomerService {

	Single<Customer> getCustomer(int id);
	
	Single<List<Customer>> getCustomersByName(String name);

	Single<Customer> insertCustomer(Customer customer);
	
	Single<Void> updateCustomer(Customer customer);

	Single<Void> deleteCustomer(int id);

	Single<CustomerOrderView> getCustomerOrderView(int id);
}