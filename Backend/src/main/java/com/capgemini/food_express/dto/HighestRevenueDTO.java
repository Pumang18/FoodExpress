package com.capgemini.food_express.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighestRevenueDTO {
	private String name;
	private String location;
	private double revenue;
}