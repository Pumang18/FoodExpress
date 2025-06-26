package com.capgemini.food_express.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capgemini.food_express.entities.User;
import com.capgemini.food_express.exceptions.UserNotFoundException;
import com.capgemini.food_express.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	final private UserRepository userRepository;
	final private PasswordEncoder passwordEncoder;
	final private EmailService emailService;
	private Map<String, String> otpStorage = new HashMap<>(); 

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	@Override
	public List<User> getAllUsers() {

		return userRepository.findAll();
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Employee not found with ID:" + id));
	}

	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateUser(Long id, User user) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Employee not found with ID:" + id));
		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
		existingUser.setPassword(user.getPassword());
		existingUser.setPhone(user.getPhone());
		existingUser.setAddress(user.getAddress());
		existingUser.setUserType(user.getUserType());
		return userRepository.save(existingUser);

	}

	@Override
	public boolean changePasswordByEmail(String email, String currentPassword, String newPassword) {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();

			if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
				return false;
			}

			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return true;
		}

		return false;
	}

	@Override
	public User patchUser(Long id, User user) {
		// TODO Auto-generated method stub
		User existing = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Employee not found with ID:" + id));
		if (user.getName() != null) {
			existing.setName(user.getName());
		}
		if (user.getEmail() != null) {
			existing.setEmail(user.getEmail());
		}
		if (user.getPhone() != null) {
			existing.setPhone(user.getPhone());
		}
		if (user.getAddress() != null) {
			existing.setAddress(user.getAddress());
		}
		if (user.getPassword() != null) {
			existing.setPassword(user.getPassword());
		}
		if (user.getUserType() != null) {
			existing.setUserType(user.getUserType());
		}
//		if (userRepository.existsByEmail(existing.getEmail())) {
//			throw new UserNotFoundException("Email is already Exists.");
//		}

		return userRepository.save(existing);
	}

	@Override
	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			new UserNotFoundException("Employee not found with ID:" + id);
		}
		userRepository.deleteById(id);

	}

	@Override
	public boolean existsByName(String name) {
		return userRepository.existsByName(name);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User findByNameOrEmail(String name, String email) {
		return userRepository.findByNameOrEmail(name, email).orElseThrow();
	}

	@Override
	public boolean generateAndSendOtp(String email) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			String otp = String.valueOf(new Random().nextInt(999999)); // Generate 6-digit OTP
			otpStorage.put(email, otp);

			emailService.sendOtpEmail(email, otp);
			return true;
		}
		return false;
	}

	@Override
	public boolean verifyOtp(String email, String otp) {
		String storedOtp = otpStorage.get(email);
		if (storedOtp != null && storedOtp.equals(otp)) {
			otpStorage.remove(email);
			return true;
		}
		return false;
	}

	@Override
	public boolean resetPassword(String email, String newPassword) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setPassword(new BCryptPasswordEncoder().encode(newPassword)); 
			userRepository.save(user);
			return true;
		}
		return false;
	}

}
