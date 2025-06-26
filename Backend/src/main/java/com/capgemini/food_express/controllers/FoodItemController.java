package com.capgemini.food_express.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.food_express.entities.FoodItem;
import com.capgemini.food_express.entities.Restaurant;
import com.capgemini.food_express.services.FoodItemService;
import com.capgemini.food_express.services.RestaurantService;
import com.capgemini.food_express.services.RestaurantServiceImpl;

@RestController
@RequestMapping("/api/foodItems")
public class FoodItemController {
	private final FoodItemService foodItemService;

	@Autowired
	public FoodItemController(FoodItemService foodItemService) {
		this.foodItemService=foodItemService;
	}

	@GetMapping
	@PreAuthorize("hasRole('Admin') or hasRole('Owner')or hasRole('Customer')")
	public ResponseEntity<List<FoodItem>> getAllFoodItems() {
		List<FoodItem> foodItems = foodItemService.getAllFoodItems();
		return ResponseEntity.status(HttpStatus.OK).body(foodItems);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('Admin') or hasRole('Owner')or hasRole('Customer')")
	public ResponseEntity<FoodItem> getFoodItem(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(foodItemService.getFoodItemById(id));
	}
	
	 @GetMapping("/menu-items")
		@PreAuthorize("hasRole('Admin') or hasRole('Owner')or hasRole('Customer')")

	    public List<FoodItem> getMenuItemsByRestaurantId(@RequestParam Long restaurantId) {
	        return foodItemService.getMenuItemsByRestaurantId(restaurantId);
	    }

	@PostMapping
	@PreAuthorize("hasRole('Owner')")
	public ResponseEntity<FoodItem> createFoodItem(@RequestBody FoodItem foodItem) {
		FoodItem saved = foodItemService.createFoodItem(foodItem);
		return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/api/fooditems/" + saved.getId()))
				.body(saved);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('Owner')")
	public ResponseEntity<FoodItem> updateFoodItem(@PathVariable Long id, @RequestBody FoodItem foodItem) {
		FoodItem updated = foodItemService.updateFoodItem(id, foodItem);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('Owner')")
	public ResponseEntity<FoodItem> deleteFoodItem(@PathVariable Long id) {
		foodItemService.deleteFoodItem(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}
}
