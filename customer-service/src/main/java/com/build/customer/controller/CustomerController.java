package com.build.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.customer.dao.CustomerDao;
import com.build.customer.domain.Customer;
import com.build.event.customer.CustomerEvent;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private Source source;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Customer getCustomer(@PathVariable("id") int id) {
		return customerDao.getCustomer(id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Customer> getCustomersByName(@RequestParam("name") String name) {
		return customerDao.getCustomersByName(name);
	}
	
	@RequestMapping(method = RequestMethod.POST) 
	public Customer insertCustomer(@RequestBody Customer customer) {
		customerDao.insertCustomer(customer);
		source.output().send(MessageBuilder.withPayload(CustomerEventHelper.createSaveEvent(customer)).build());
		return customer;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void updateCustomer(@PathVariable("id") int id, @RequestBody Customer customer) {
		customer.setId(id);
		customerDao.updateCustomer(customer);
		source.output().send(MessageBuilder.withPayload(CustomerEventHelper.createSaveEvent(customer)).build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteCustomer(@PathVariable("id") int id) {
		customerDao.deleteCustomer(id);
		source.output().send(MessageBuilder.withPayload(CustomerEvent.delete(id)).build());
	}
}
