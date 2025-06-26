package com.capgemini.food_express.exceptions;

public class FoodItemNotFoundException extends RuntimeException {

	public FoodItemNotFoundException(String message) {
		super(message);
	}
}
