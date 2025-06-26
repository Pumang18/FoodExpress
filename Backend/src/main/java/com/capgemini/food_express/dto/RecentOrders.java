package com.capgemini.food_express.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentOrders {
	 private Long id;
	    private LocalDateTime orderDate;
	    private Double totalAmount;
	    private String customerName;
	    private Integer totalItems;
}
