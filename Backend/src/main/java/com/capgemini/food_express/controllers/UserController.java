package com.capgemini.food_express.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.food_express.dto.PasswordChangeRequest;
import com.capgemini.food_express.entities.User;
import com.capgemini.food_express.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
	}

	@PostMapping
	@PreAuthorize("hasRole('Owner') or hasRole('Admin') or hasRole('Customer')")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User saved = userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/api/users/" + saved.getId()))
				.body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
		System.err.print("Inside Update");
		User updated = userService.updateUser(id, user);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}
	
	@PatchMapping("/{id}")	
	public ResponseEntity<User> patchUser(@PathVariable Long id , @RequestBody User patch){
		System.err.print("Inside Patch");
		return ResponseEntity.status(HttpStatus.OK).body(userService.patchUser(id, patch));
	}
	
	@PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        boolean success = userService.changePasswordByEmail(
            request.getEmail(),
            request.getCurrentPassword(),
            request.getNewPassword()
        );

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid current password or user not found.");
        }

        return ResponseEntity.ok("Password updated successfully.");
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

}
