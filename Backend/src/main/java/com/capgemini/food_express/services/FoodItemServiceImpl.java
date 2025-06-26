package com.capgemini.food_express.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capgemini.food_express.FoodExpressApplication;
import com.capgemini.food_express.entities.FoodItem;
import com.capgemini.food_express.entities.Restaurant;
import com.capgemini.food_express.repositories.FoodItemRepo;

@Service
public class FoodItemServiceImpl implements FoodItemService {


	final private FoodItemRepo foodItemRepo;
	
	@Autowired
	public FoodItemServiceImpl(FoodItemRepo foodItemRepo, FoodExpressApplication foodExpressApplication) {
		this.foodItemRepo=foodItemRepo;
	}
	
	@Override
	public List<FoodItem> getAllFoodItems() {
		return foodItemRepo.findAll();
	}

	@Override
	public FoodItem getFoodItemById(Long id) {
		return foodItemRepo.findById(id).orElseThrow();
	}

	@Override
	public FoodItem createFoodItem(FoodItem foodItem) {

		return foodItemRepo.save(foodItem);
	}

	@Override
	public FoodItem updateFoodItem(Long id, FoodItem foodItem) {
		FoodItem res = getFoodItemById(id);
		res.setName(foodItem.getName());
		res.setCategory(foodItem.getCategory());
		res.setPrice(foodItem.getPrice());
		res.setRestaurantId(foodItem.getRestaurantId());
		return foodItemRepo.save(res);
	}
	
	@Override
	public List<FoodItem> getMenuItemsByRestaurantId(Long restaurantId){
		return foodItemRepo.findByRestaurantId(restaurantId);
	}

	@Override
	public void deleteFoodItem(Long id) {
		foodItemRepo.deleteById(id);		
	}

}
