package com.capgemini.food_express.services;

import java.util.List;
import java.util.Optional;

import com.capgemini.food_express.entities.*;

public interface RestaurantService {
	List<Restaurant> getAllRestaurants();

	Restaurant getRestaurantById(Long id);
	
	Restaurant getByUserId(Long id);

	Restaurant createRestaurant(Restaurant restaurant);
	
	Restaurant updateRestaurant(Long id,Restaurant restaurant);

	void deleteRestaurant(Long id);
}
