package com.capgemini.food_express.services;

import java.util.List;

import com.capgemini.food_express.entities.User;

public interface UserService {
	
	List<User> getAllUsers();
	
	User getUserById(Long id);
	
	User createUser(User user);
	
	boolean changePasswordByEmail(String email, String currentPassword, String newPassword);
	
	User updateUser(Long id,User user);
	
	void deleteUser(Long id);
	
	boolean existsByName(String name);

	boolean existsByEmail(String email);

	User findByNameOrEmail(String name, String email);
	
	boolean generateAndSendOtp(String email);
	
	boolean verifyOtp(String email, String otp);
	
	boolean resetPassword(String email, String newPassword);

	User patchUser(Long id, User user);
}
