package com.capgemini.food_express.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.food_express.dto.*;
import com.capgemini.food_express.services.OrderService;
import com.capgemini.food_express.services.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private ReportService reportService;
    private OrderService orderService;
    
    

    public ReportController(ReportService reportService, OrderService orderService) {
		super();
		this.reportService = reportService;
		this.orderService = orderService;
	}

	@GetMapping("/highest-revenue")
	@PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<HighestRevenueDTO>> getHighestRevenue() {
        return ResponseEntity.ok(reportService.getHighestRevenueReport());
    }

    @GetMapping("/most-orders")
	@PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<MostOrdersDTO>> getMostOrders() {
        return ResponseEntity.ok(reportService.getMostOrdersReport());
    }

    @GetMapping("/items-sold")
	@PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<ItemsSoldDTO>> getItemsSold() {
        return ResponseEntity.ok(reportService.getItemsSoldReport());
    }
    
    @GetMapping("/top-items/{restaurantId}")
	@PreAuthorize("hasRole('Owner')")
    public ResponseEntity<List<TopSellingItems>> getTopItems(@PathVariable Long restaurantId) {
        List<TopSellingItems> data = reportService.getTopSellingItemsForRestaurant(restaurantId);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/recent-orders/{restaurantId}")
	@PreAuthorize("hasRole('Owner')")
    public ResponseEntity<List<RecentOrders>> getRecentOrders(@PathVariable Long restaurantId) {
        List<RecentOrders> data = reportService.getRecentOrdersForRestaurant(restaurantId);
        return ResponseEntity.ok(data);
    }
}
