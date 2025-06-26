package com.capgemini.food_express.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capgemini.food_express.entities.OrderItem;
import com.capgemini.food_express.repositories.OrderItemRepo;

@Service
public class OrderItemServiceImpl implements OrderItemService{

	final private OrderItemRepo orderItemRepo;
	
	
	public OrderItemServiceImpl(OrderItemRepo orderItemRepo) {
		super();
		this.orderItemRepo = orderItemRepo;
	}

	@Override
	public List<OrderItem> getAllOrderItems() {
		return orderItemRepo.findAll();
	}

	@Override
	public OrderItem getOrderItemById(Long id) {
		return orderItemRepo.findById(id).orElseThrow();
	}

	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		return orderItemRepo.save(orderItem);
	}

	@Override
	public OrderItem updateOrderItem(Long id, OrderItem orderItem) {
		OrderItem res = getOrderItemById(id);
		res.setItemId(orderItem.getItemId());
		res.setOrderId(orderItem.getOrderId());
		res.setQuantity(orderItem.getQuantity());
		return orderItemRepo.save(res);
	}

	@Override
	public void deleteOrderItem(Long id) {
		orderItemRepo.deleteById(id);
	}
	
	@Override
	public List<OrderItem> getByOrderItemByOrderId(Long orderId) {
		return orderItemRepo.findByOrderId(orderId);
	}

}
