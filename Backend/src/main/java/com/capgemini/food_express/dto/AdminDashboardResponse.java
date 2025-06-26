package com.capgemini.food_express.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private int totalRestaurants;
    private int totalUsers;
    private int totalOrders;
    private double totalRevenue;
    private List<SalesPerRestaurant> salesPerRestaurant;
    private Map<String, Integer> ordersPerDay;
}
