package com.capgemini.food_express.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDashboardResponse {
	private long totalOrders;
    private double totalRevenue;
    private double avgOrderValue;
    private List<ItemStats> topItems;
    private Map<String, Long> ordersOverTime;
    private Map<String, Integer> categoryDistribution;
}
