package com.build.coordination.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import com.build.coordination.AsyncUtils;
import com.build.coordination.domain.Customer;
import com.build.coordination.domain.CustomerOrderView;
import com.build.coordination.domain.Order;
import com.build.coordination.order.OrderService;

import rx.Observable;
import rx.Single;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private AsyncRestTemplate restTemplate;

	@Autowired
	private OrderService orderService;

	@Override
	public Single<Customer> getCustomer(int id) {
		return AsyncUtils.fromResponseEntityFuture(
				restTemplate.getForEntity("http://customer-service/customers/{id}", Customer.class, id));
	}
	
	@Override
	public Single<List<Customer>> getCustomersByName(String name) {
		return AsyncUtils
				.fromResponseEntityFuture(restTemplate.exchange("http://customer-service/customers?name={name}",
						HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {
						}, name));
	}

	@Override
	public Single<Customer> insertCustomer(Customer customer) {
		HttpEntity<Customer> request = new HttpEntity<>(customer);
		return AsyncUtils.fromResponseEntityFuture(
				restTemplate.postForEntity("http://customer-service/customers", request, Customer.class));
	}

	@Override
	public Single<Void> updateCustomer(Customer customer) {
		HttpEntity<Customer> request = new HttpEntity<>(customer);
		return AsyncUtils
				.fromVoidFuture(restTemplate.put("http://customer-service/customers/{id}", request, customer.getId()));
	}

	@Override
	public Single<Void> deleteCustomer(int id) {
		return AsyncUtils.fromVoidFuture(restTemplate.delete("http://customer-service/customers/{id}", id));
	}

	@Override
	public Single<CustomerOrderView> getCustomerOrderView(int id) {
		Single<Customer> customerObs = AsyncUtils.fromResponseEntityFuture(
				restTemplate.getForEntity("http://customer-service/customers/{id}", Customer.class, id));
		Observable<List<Order>> ordersObs = orderService.getCustomerOrders(id);
		return customerObs.zipWith(ordersObs.toSingle(), (c, ol) -> new CustomerOrderView(c, ol));
	}

}
