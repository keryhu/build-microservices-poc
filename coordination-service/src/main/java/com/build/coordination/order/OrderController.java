package com.build.coordination.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.build.coordination.customer.CustomerService;
import com.build.coordination.domain.Customer;
import com.build.coordination.domain.Order;

import rx.Single;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Single<Order> getOrder(@PathVariable("id") int id) {
		return orderService.getOrder(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Single<Order> insertOrder(@RequestBody Order order) {
		Single<Customer> customerObs = customerService.getCustomer(order.getCustomerId());
		return customerObs.flatMap(c -> {
			return orderService.insertOrder(order);
		});
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Single<Void> updateOrder(@PathVariable("id") int id, @RequestBody Order order) {
		order.setId(id);
		return orderService.updateOrder(order);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Single<Void> deleteOrder(@PathVariable("id") int id) {
		return orderService.deleteOrder(id);
	}
}
