package com.capgemini.food_express.services;

import java.util.List;

import com.capgemini.food_express.entities.Order;
import com.capgemini.food_express.entities.OrderItem;

public interface OrderItemService {

	List<OrderItem> getAllOrderItems();

	OrderItem getOrderItemById(Long id);

	OrderItem createOrderItem(OrderItem orderItem);

	OrderItem updateOrderItem(Long id, OrderItem orderItem);
	
	List<OrderItem> getByOrderItemByOrderId(Long orderId);

	void deleteOrderItem(Long id);
}
