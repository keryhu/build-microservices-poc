package com.build.coordination.order;

import java.util.List;

import com.build.coordination.domain.Order;

import rx.Observable;
import rx.Single;

public interface OrderService {

	Single<Order> getOrder(int id);
	
	Single<Order> insertOrder(Order order);
	
	Single<Void> updateOrder(Order order);

	Single<Void> deleteOrder(int id);
	
	Observable<List<Order>> getCustomerOrders(int customerId);

}