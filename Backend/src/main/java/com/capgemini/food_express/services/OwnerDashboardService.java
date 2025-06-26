package com.capgemini.food_express.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.food_express.dto.ItemStats;
import com.capgemini.food_express.dto.OwnerDashboardResponse;
import com.capgemini.food_express.entities.*;
import com.capgemini.food_express.repositories.FoodItemRepo;
import com.capgemini.food_express.repositories.OrderItemRepo;
import com.capgemini.food_express.repositories.OrderRepo;

@Service
public class OwnerDashboardService {
	@Autowired
	private OrderRepo orderRepository;
	@Autowired
	private OrderItemRepo orderItemRepository;
	@Autowired
	private FoodItemRepo foodItemRepository;

	public OwnerDashboardResponse getDashboardData(Long restaurantId) {
		List<Order> orders = orderRepository.findByRestaurantId(restaurantId);

		long totalOrders = orders.size();
		double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();
		double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;

		List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
		List<OrderItem> orderItems = orderItemRepository.findByOrderIdIn(orderIds);
		List<FoodItem> foodItems = foodItemRepository.findAll();

		Map<Long, Integer> itemCountMap = new HashMap<>();
		for (OrderItem item : orderItems) {
			itemCountMap.merge(item.getItemId(), item.getQuantity(), Integer::sum);
		}

		List<ItemStats> topItems = itemCountMap.entrySet().stream().map(entry -> {
			String name = foodItems.stream().filter(f -> f.getId().equals(entry.getKey())).map(FoodItem::getName)
					.findFirst().orElse("Unknown");
			return new ItemStats(name, entry.getValue());
		}).sorted((a, b) -> b.getQuantity() - a.getQuantity()).limit(5).collect(Collectors.toList());

		Map<String, Long> ordersOverTime = orders.stream().collect(Collectors
				.groupingBy((Function<Order, String>) o -> o.getOrderDate().toString(), Collectors.counting()));

		Map<String, Integer> categoryDistribution = new HashMap<>();
		for (OrderItem item : orderItems) {
			FoodItem food = foodItems.stream().filter(f -> f.getId().equals(item.getItemId())).findFirst().orElse(null);

			if (food != null) {
				categoryDistribution.merge(food.getCategory(), item.getQuantity(), Integer::sum);
			}
		}

		return new OwnerDashboardResponse(totalOrders, totalRevenue, avgOrderValue, topItems, ordersOverTime,
				categoryDistribution);
	}
}
