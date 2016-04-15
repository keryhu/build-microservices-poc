package com.build.coordination.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.coordination.domain.Customer;
import com.build.coordination.domain.CustomerOrderView;

import rx.Single;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Single<Customer> getCustomer(@PathVariable("id") int id) {
		return customerService.getCustomer(id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public Single<List<Customer>> getCustomersByName(@RequestParam("name") String name) {
		return customerService.getCustomersByName(name);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Single<Customer> insertCustomer(@RequestBody Customer customer) {
		return customerService.insertCustomer(customer);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Single<Void> updateCustomer(@PathVariable("id") int id, @RequestBody Customer customer) {
		customer.setId(id);
		return customerService.updateCustomer(customer);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Single<Void> deleteCustomer(@PathVariable("id") int id) {
		return customerService.deleteCustomer(id);
	}

	@RequestMapping(value = "/{id}/orders", method = RequestMethod.GET)
	public Single<CustomerOrderView> getCustomerOrderView(@PathVariable("id") int id) {
		return customerService.getCustomerOrderView(id);
	}
}
