package com.capgemini.food_express.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.food_express.entities.*;
import java.util.List;


@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Long>{
	Restaurant findByUserId(Long userId);
}
