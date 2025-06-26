package com.capgemini.food_express.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.food_express.dto.ForgotPasswordRequest;
import com.capgemini.food_express.dto.LoginDto;
import com.capgemini.food_express.dto.ResetPasswordRequest;
import com.capgemini.food_express.dto.ResponseToken;
import com.capgemini.food_express.dto.VerifyOtpRequest;
import com.capgemini.food_express.entities.Restaurant;
import com.capgemini.food_express.entities.User;
import com.capgemini.food_express.exceptions.InvalidCredentialsException;
import com.capgemini.food_express.exceptions.UserAlreadyExistsException;
import com.capgemini.food_express.security.CustomUserDetailsService;
import com.capgemini.food_express.security.JwtUtils;
import com.capgemini.food_express.services.RestaurantService;
import com.capgemini.food_express.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final CustomUserDetailsService customUserDetailsService;

	AuthenticationManager authenticationManager;
	UserService userService;
	RestaurantService restaurantService;
	PasswordEncoder passwordEncoder;
	JwtUtils jwtService;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserService userService,
			PasswordEncoder passwordEncoder, JwtUtils jwtService, CustomUserDetailsService customUserDetailsService,
			RestaurantService restaurantService) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.customUserDetailsService = customUserDetailsService;
		this.restaurantService = restaurantService;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) throws InvalidCredentialsException{
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

		if (authentication.isAuthenticated()) {
			User user = userService.findByNameOrEmail(loginDto.getEmail(), loginDto.getEmail());
			Map<String, Object> claims = new HashMap<>();
			claims.put("email", user.getEmail());
			claims.put("userid", user.getId());
			claims.put("usertype", user.getUserType());
			if (user.getUserType().equals("Owner")) {
				Restaurant restaurant = restaurantService.getByUserId(user.getId());
				claims.put("restaurantId", restaurant.getId());
				claims.put("restaurantName", restaurant.getName());
			}
			String token = jwtService.generateToken(loginDto.getEmail(), claims);
			System.err.println(token);
			Map<String, String> response = new HashMap<>();
			response.put("token", token);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not Authorized !!");
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		if (userService.existsByName(user.getEmail()) || userService.existsByEmail(user.getEmail()))
			throw new UserAlreadyExistsException("Username or Email Exists !");
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(user));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
		String email = request.getEmail();

		// Generate OTP and send it to the user's email
		boolean isOtpSent = userService.generateAndSendOtp(email);
		if (isOtpSent) {
			return ResponseEntity.ok("OTP sent to your email.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found.");
		}
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
		boolean isOtpValid = userService.verifyOtp(request.getEmail(), request.getOtp());
		if (isOtpValid) {
			return ResponseEntity.ok("OTP verified successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
		}
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
	    boolean isPasswordReset = userService.resetPassword(request.getEmail(), request.getNewPassword());
	    if (isPasswordReset) {
	        return ResponseEntity.ok("Password reset successfully.");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reset password.");
	    }
	}
}
