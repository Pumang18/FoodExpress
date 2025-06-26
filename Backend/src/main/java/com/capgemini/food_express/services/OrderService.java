package com.capgemini.food_express.services;

import java.util.List;
import java.util.Map;

import com.capgemini.food_express.entities.Order;

public interface OrderService {

	List<Order> getAllOrders();
	
	Order getOrderById(Long id);
	
	Order createOrder(Order order);
	
	Order updateOrder(Long id,Order order);
	
	List<Order> getOrdersByUserId(Long userId);
	
	List<Order> getOrdersByRestaurantId(Long restaurantId);
	
	List<Map<String, Object>> getOrdersByRestaurantWithCustomerName(Long restaurantId);
	
	List<Map<String, Object>> getAllOrdersWithCustomerName();
	
	List<Map<String, Object>> getOrdersWithItemsForRestaurant(Long restaurantId);
	
	Map<String, Object> getOrderDetails(Long orderId);
	
	void deleteOrder(Long id);
}
