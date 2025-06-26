package com.capgemini.food_express.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.capgemini.food_express.entities.FoodItem;
import com.capgemini.food_express.entities.Order;
import com.capgemini.food_express.entities.OrderItem;
import com.capgemini.food_express.entities.User;
import com.capgemini.food_express.repositories.FoodItemRepo;
import com.capgemini.food_express.repositories.OrderItemRepo;
import com.capgemini.food_express.repositories.OrderRepo;
import com.capgemini.food_express.repositories.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

	final private OrderRepo orderRepo;
	final private UserRepository userRepo;
	final private OrderItemRepo orderItemRepo;
	final private FoodItemRepo foodItemRepo;

	public OrderServiceImpl(OrderRepo orderRepo,UserRepository userRepo,OrderItemRepo orderItemRepo,FoodItemRepo foodItemRepo) {
		this.orderRepo = orderRepo;
		this.userRepo=userRepo;
		this.orderItemRepo=orderItemRepo;
		this.foodItemRepo=foodItemRepo;
	}

	@Override
	public List<Order> getAllOrders() {
		// TODO Auto-generated method stub
		return orderRepo.findAll();
	}

	@Override
	public Order getOrderById(Long id) {
		// TODO Auto-generated method stub
		return orderRepo.findById(id).orElseThrow();
	}

	@Override
	public Order createOrder(Order order) {
		// TODO Auto-generated method stub
		return orderRepo.save(order);
	}

	@Override
	public Order updateOrder(Long id, Order order) {
		Order res = getOrderById(id);
		res.setRestaurantId(order.getRestaurantId());
		res.setOrderDate(order.getOrderDate());
		res.setTotalAmount(order.getTotalAmount());
		res.setUserId(order.getUserId());
		return orderRepo.save(res);
	}
	
	@Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepo.findByUserId(userId);
    }
	
	@Override
	public List<Order> getOrdersByRestaurantId(Long restaurantId) {
		return orderRepo.findByRestaurantId(restaurantId);
	}
	
	@Override
	public List<Map<String, Object>> getOrdersByRestaurantWithCustomerName(Long restaurantId) {
		 List<Order> orders = orderRepo.findByRestaurantId(restaurantId);
		    return orders.stream().map(order -> {
		        User user = userRepo.findById(order.getUserId()).orElse(null);
		        Map<String, Object> map = new HashMap<>();
		        map.put("id", order.getId());
		        map.put("orderDate", order.getOrderDate());
		        map.put("totalAmount", order.getTotalAmount());
		        map.put("userId", order.getUserId());
		        map.put("customerName", user != null ? user.getName() : "Unknown");
		        return map;
		    }).collect(Collectors.toList());
	}
	
	
	
	@Override
	public Map<String, Object> getOrderDetails(Long orderId) {
	    Order order = orderRepo.findById(orderId).orElse(null);
	    if (order == null) {
	        throw new RuntimeException("Order not found with id: " + orderId);
	    }

	    User user = userRepo.findById(order.getUserId()).orElse(null);
	    List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
	    
	    List<Map<String, Object>> detailedItems = items.stream().map(item -> {
	        FoodItem food = foodItemRepo.findById(item.getItemId()).orElse(null);
	        Map<String, Object> itemMap = new HashMap<>();
	        itemMap.put("name", food != null ? food.getName() : "Unknown");
	        itemMap.put("price", food != null ? food.getPrice() : 0);
	        itemMap.put("quantity", item.getQuantity());
	        return itemMap;
	    }).collect(Collectors.toList());

	    Map<String, Object> orderDetails = new HashMap<>();
	    orderDetails.put("id", order.getId());
	    orderDetails.put("orderDate", order.getOrderDate());
	    orderDetails.put("totalAmount", order.getTotalAmount());
	    orderDetails.put("customerName", user != null ? user.getName() : "Unknown");
	    orderDetails.put("items", detailedItems);

	    return orderDetails;
	}
	
	@Override
	public List<Map<String, Object>> getOrdersWithItemsForRestaurant(Long restaurantId) {
        List<Order> orders = orderRepo.findByRestaurantId(restaurantId);

        List<Map<String, Object>> response = new ArrayList<>();

        for (Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderDate", order.getOrderDate());
            orderMap.put("totalAmount", order.getTotalAmount());

            // Customer name
            userRepo.findById(order.getUserId())
                .ifPresentOrElse(
                    user -> orderMap.put("customerName", user.getName()),
                    () -> orderMap.put("customerName", "Unknown")
                );

            // Items
            List<Map<String, Object>> itemList = new ArrayList<>();
            List<OrderItem> orderItems = orderItemRepo.findByOrderId(order.getId());

            for (OrderItem item : orderItems) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("quantity", item.getQuantity());

                foodItemRepo.findById(item.getItemId())
                    .ifPresentOrElse(
                        food -> {
                            itemMap.put("name", food.getName());
                            itemMap.put("price", food.getPrice());
                        },
                        () -> {
                            itemMap.put("name", "Unknown");
                            itemMap.put("price", 0.0);
                        }
                    );

                itemList.add(itemMap);
            }

            orderMap.put("items", itemList);

            response.add(orderMap);
        }

        return response;
    }
	
	@Override
	public List<Map<String, Object>> getAllOrdersWithCustomerName() {
	    List<Order> orders = orderRepo.findAll();
	    return orders.stream().map(order -> {
	        User user = userRepo.findById(order.getUserId()).orElse(null);
	        Map<String, Object> map = new HashMap<>();
	        map.put("id", order.getId());
	        map.put("orderDate", order.getOrderDate());
	        map.put("totalAmount", order.getTotalAmount());
	        map.put("userId", order.getUserId());
	        map.put("restaurantId", order.getRestaurantId());
	        map.put("customerName", user != null ? user.getName() : "Unknown");
	        return map;
	    }).collect(Collectors.toList());
	}

	@Override
	public void deleteOrder(Long id) {
		orderRepo.deleteById(id);

	}

}
