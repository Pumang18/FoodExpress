package com.capgemini.food_express.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.capgemini.food_express.services.OrderServiceImpl;
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
import com.capgemini.food_express.entities.Order;
import com.capgemini.food_express.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService, OrderServiceImpl orderServiceImpl) {
		this.orderService = orderService;
	}

//	@GetMapping
//    public List<Order> getOrdersByUserId(@RequestParam Long userId) {
//        return orderService.getOrdersByUserId(userId);
//    }
//	
	@GetMapping
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
	public ResponseEntity<List<Map<String, Object>>> getOrdersWithCustomerName(
	        @RequestParam(required = false) Long restaurantId) {
	    
	    List<Map<String, Object>> result;
	    
	    if (restaurantId != null) {
	        result = orderService.getOrdersByRestaurantWithCustomerName(restaurantId);
	    } else {
	        result = orderService.getAllOrdersWithCustomerName();
	    }

	    return ResponseEntity.ok(result);
	}

    @GetMapping("/by-user")
	@PreAuthorize("hasRole('Customer')")
    public ResponseEntity<List<Order>> getOrdersByUserId(@RequestParam Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/byRestaurantWithCustomer")
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    public ResponseEntity<List<Map<String, Object>>> getOrdersByRestaurantWithCustomer(@RequestParam Long restaurantId) {
        return ResponseEntity.ok(orderService.getOrdersByRestaurantWithCustomerName(restaurantId));
    }

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
	public ResponseEntity<Order> getFoodItem(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(id));
	}
	
	@GetMapping("/restaurant/{restaurantId}")
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    public ResponseEntity<List<Map<String, Object>>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderService.getOrdersWithItemsForRestaurant(restaurantId));
    }
	
	@GetMapping("/{id}/details")
	@PreAuthorize("hasRole('Owner') or hasRole('Admin')")
	public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable Long id) {
	    try {
	        Map<String, Object> orderDetails = orderService.getOrderDetails(id);
	        return ResponseEntity.ok(orderDetails);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}


	@PostMapping
	@PreAuthorize("hasRole('Customer')")
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		Order saved = orderService.createOrder(order);
		return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/api/orders/" + saved.getId()))
				.body(saved);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('Customer')")
	public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
		Order updated = orderService.updateOrder(id, order);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

}
