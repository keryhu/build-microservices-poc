package com.build.coordination.order;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import com.build.coordination.AsyncUtils;
import com.build.coordination.domain.Order;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import rx.Observable;
import rx.Single;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private AsyncRestTemplate restTemplate;
	
	@Override
	public Single<Order> getOrder(int id) {
		return AsyncUtils.fromResponseEntityFuture(restTemplate.getForEntity("http://order-service/orders/{id}", Order.class, id));
	}
	
	@Override
	public Single<Order> insertOrder(Order order) {
		HttpEntity<Order> request = new HttpEntity<>(order);
		return AsyncUtils.fromResponseEntityFuture(restTemplate.postForEntity("http://order-service/orders", request, Order.class));
	}
	
	@Override
	public Single<Void> updateOrder(Order order) {
		HttpEntity<Order> request = new HttpEntity<>(order);
		return AsyncUtils.fromVoidFuture(restTemplate.put("http://order-service/orders/{id}", request, order.getId()));
	}
	
	@Override
	public Single<Void> deleteOrder(int id) {
		return AsyncUtils.fromVoidFuture(restTemplate.delete("http://order-service/orders/{id}", id));
	}
	
	public List<Order> getCustomerOrdersFallback(int customerId) {
		return Collections.emptyList();
	}
	
	@HystrixCommand(fallbackMethod="getCustomerOrdersFallback")
	@Override
	public Observable<List<Order>> getCustomerOrders(int customerId) {
		return AsyncUtils.fromResponseEntityFuture(restTemplate.exchange(
				"http://order-service/orders?customerId={customerId}", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Order>>() {
				}, customerId)).toObservable();
	}
}
