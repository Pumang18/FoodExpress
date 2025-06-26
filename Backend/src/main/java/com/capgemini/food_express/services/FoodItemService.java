package com.capgemini.food_express.services;

import java.util.List;

import com.capgemini.food_express.entities.FoodItem;

public interface FoodItemService {
	
	List<FoodItem> getAllFoodItems();
	
	FoodItem getFoodItemById(Long id);
	
	FoodItem createFoodItem(FoodItem foodItem);
	
	FoodItem updateFoodItem(Long id,FoodItem foodItem);
	
	List<FoodItem> getMenuItemsByRestaurantId(Long restaurantId);
	
	void deleteFoodItem(Long id);
	
}
