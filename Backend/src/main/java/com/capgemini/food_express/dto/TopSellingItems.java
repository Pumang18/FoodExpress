package com.capgemini.food_express.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopSellingItems {
	private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer qty;
}
