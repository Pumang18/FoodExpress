package com.capgemini.food_express.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostOrdersDTO {
	private String name;
	private String email;
	private long totalOrders;
}
