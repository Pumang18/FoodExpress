package com.capgemini.food_express.services;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.food_express.dto.AdminDashboardResponse;
import com.capgemini.food_express.dto.SalesPerRestaurant;
import com.capgemini.food_express.entities.Order;
import com.capgemini.food_express.entities.Restaurant;
import com.capgemini.food_express.entities.User;
import com.capgemini.food_express.repositories.OrderRepo;
import com.capgemini.food_express.repositories.RestaurantRepo;
import com.capgemini.food_express.repositories.UserRepository;

@Service
public class AdminDashboardService {

    @Autowired
    private RestaurantRepo restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepo orderRepository;

    public AdminDashboardResponse getDashboardData() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<User> users = userRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        int totalRestaurants = restaurants.size();
        int totalUsers = users.size();
        int totalOrders = orders.size();

        double totalRevenue = orders.stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0.0)
                .sum();

        // Sales per restaurant
        List<SalesPerRestaurant> salesData = restaurants.stream()
                .map(r -> {
                    double totalSales = orders.stream()
                            .filter(o -> o.getRestaurantId().equals(r.getId()))
                            .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0.0)
                            .sum();
                    return new SalesPerRestaurant(r.getName(), totalSales);
                }).collect(Collectors.toList());

        // Orders per day
        Map<String, Integer> ordersPerDay = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        for (Order order : orders) {
            String date = order.getOrderDate().format(formatter);
            ordersPerDay.put(date, ordersPerDay.getOrDefault(date, 0) + 1);
        }

        return new AdminDashboardResponse(
                totalRestaurants,
                totalUsers,
                totalOrders,
                totalRevenue,
                salesData,
                ordersPerDay
        );
    }
}

