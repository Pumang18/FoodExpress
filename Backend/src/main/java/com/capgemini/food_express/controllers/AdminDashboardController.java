package com.capgemini.food_express.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.food_express.dto.AdminDashboardResponse;
import com.capgemini.food_express.dto.OwnerDashboardResponse;
import com.capgemini.food_express.services.AdminDashboardService;
import com.capgemini.food_express.services.OwnerDashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService dashboardService;
    
    @Autowired 
    private OwnerDashboardService ownerDashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
    
    @GetMapping("/owner")
    @PreAuthorize("hasRole('Owner')")
    public ResponseEntity<OwnerDashboardResponse> getDashboard(@RequestParam Long restaurantId) {
        OwnerDashboardResponse data = ownerDashboardService.getDashboardData(restaurantId);
        return ResponseEntity.ok(data);
    }
    
}
