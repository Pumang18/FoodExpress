package com.capgemini.food_express.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.food_express.entities.FoodItem;

public interface FoodItemRepo extends JpaRepository<FoodItem, Long>{
	List<FoodItem> findByRestaurantId(Long restaurantId);
}
