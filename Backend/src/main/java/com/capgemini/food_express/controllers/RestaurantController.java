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
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.food_express.entities.Restaurant;
import com.capgemini.food_express.entities.User;
import com.capgemini.food_express.services.RestaurantService;
import com.capgemini.food_express.services.RestaurantServiceImpl;
import com.capgemini.food_express.services.UserService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;

	@Autowired
	public  RestaurantController(RestaurantService restaurantService, RestaurantServiceImpl restaurantServiceImpl) {
		this.restaurantService=restaurantService;
	}

	@GetMapping
	@PreAuthorize("hasRole('Owner') or hasRole('Admin') or hasRole('Customer')")
	public ResponseEntity<List<Restaurant>> getAllRestaurants() {
		List<Restaurant> restaurants = restaurantService.getAllRestaurants();
		return ResponseEntity.status(HttpStatus.OK).body(restaurants);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('Owner') or hasRole('Admin') or hasRole('Customer')")
	public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantById(id));
	}

	@PostMapping
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
	public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
		Restaurant saved = restaurantService.createRestaurant(restaurant);
		return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/api/restaurants/" + saved.getId()))
				.body(saved);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
	public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
		Restaurant updated = restaurantService.updateRestaurant(id, restaurant);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
	public ResponseEntity<Restaurant> deleteRestaurant(@PathVariable Long id) {
		restaurantService.deleteRestaurant(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}
}
