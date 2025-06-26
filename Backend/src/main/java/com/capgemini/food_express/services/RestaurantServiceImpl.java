package com.capgemini.food_express.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.food_express.entities.*;
import com.capgemini.food_express.repositories.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	final private RestaurantRepo foodRepo;

	@Autowired
	public RestaurantServiceImpl(RestaurantRepo foodRepo) {
		this.foodRepo = foodRepo;
	}

	@Override
	public List<Restaurant> getAllRestaurants() {

		return foodRepo.findAll();
	}

	@Override
	public Restaurant getRestaurantById(Long restaurantId) {
		// TODO Auto-generated method stub
		return foodRepo.findById(restaurantId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Restaurant ID: " + restaurantId));
		
	}

	@Override
	public Restaurant createRestaurant(Restaurant restaurant) {
		// TODO Auto-generated method stub
		return foodRepo.save(restaurant);
	}

	@Override
	public void deleteRestaurant(Long restaurantId) {
		foodRepo.deleteById(restaurantId);
		// TODO Auto-generated method stub

	}

	@Override
	public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
		Restaurant res = getRestaurantById(id);
		res.setName(restaurant.getName());
		res.setLocation(restaurant.getLocation());
		res.setContact(restaurant.getContact());
		return foodRepo.save(res);

	}

	@Override
	public Restaurant getByUserId(Long id) {
		return foodRepo.findByUserId(id);
	}

}
