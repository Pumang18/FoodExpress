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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.food_express.entities.Order;
import com.capgemini.food_express.entities.OrderItem;
import com.capgemini.food_express.services.OrderItemService;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemController {
	private final OrderItemService orderItemService;

	@Autowired
	public OrderItemController(OrderItemService orderItemService) {
		this.orderItemService = orderItemService;
	}

	
	@GetMapping("")
	@PreAuthorize("hasRole('Customer') or hasRole('Admin') or hasRole('Owner')")
	public ResponseEntity<List<OrderItem>> getOrderItems(@RequestParam(required = false) Long orderId) {
	    List<OrderItem> items;
	    if (orderId != null) {
	        items = orderItemService.getByOrderItemByOrderId(orderId);
	    } else {
	        items = orderItemService.getAllOrderItems();
	    }
	    return ResponseEntity.status(HttpStatus.OK).body(items);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('Customer') or hasRole('Admin') or hasRole('Owner')")

	public ResponseEntity<OrderItem> getOrderItem(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(orderItemService.getOrderItemById(id));
	}
	
	

	@PostMapping
	@PreAuthorize("hasRole('Customer') or hasRole('Admin') or hasRole('Owner')")
	public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
		OrderItem saved = orderItemService.createOrderItem(orderItem);
		return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/api/orderitems/" + saved.getId()))
				.body(saved);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('Customer') or hasRole('Admin') or hasRole('Owner')")
	public ResponseEntity<OrderItem> updateOrder(@PathVariable Long id, @RequestBody OrderItem orderItem) {
		OrderItem updated = orderItemService.updateOrderItem(id, orderItem);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('Customer') or hasRole('Admin') or hasRole('Owner')")
	public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
		orderItemService.deleteOrderItem(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}
}
