package com.capgemini.food_express.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capgemini.food_express.dto.HighestRevenueDTO;
import com.capgemini.food_express.dto.MostOrdersDTO;
import com.capgemini.food_express.entities.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);

	List<Order> findByRestaurantId(Long restaurantId);

	@Query("SELECT new com.capgemini.food_express.dto.HighestRevenueDTO(r.name, r.location, SUM(o.totalAmount)) "
			+ "FROM Order o JOIN Restaurant r ON o.restaurantId = r.id "
			+ "GROUP BY r.id, r.name, r.location ORDER BY SUM(o.totalAmount) DESC")
	List<HighestRevenueDTO> getHighestRevenueReport();

	@Query(value = """
			SELECT u.name, u.email, COUNT(o.id) AS totalOrders
			FROM orders o
			JOIN user u ON o.user_id = u.id
			GROUP BY u.id, u.name, u.email
			ORDER BY totalOrders DESC
			""", nativeQuery = true)
	List<Object[]> getMostOrdersReportRaw();
	
	
	@Query(value = """
		    SELECT o.id AS order_id,
		           o.order_date AS order_date,
		           o.total_amount AS total_amount,
		           u.name AS customer_name,
		           COALESCE(SUM(oi.quantity), 0) AS total_items
		    FROM orders o
		    JOIN user u ON o.user_id = u.id
		    LEFT JOIN order_item oi ON o.id = oi.order_id
		    WHERE o.restaurant_id = :restaurantId
		    GROUP BY o.id, o.order_date, o.total_amount, u.name
		    ORDER BY o.order_date DESC
		    LIMIT 5
		    """, nativeQuery = true)
		List<Object[]> findRecentOrdersWithItemCount(@Param("restaurantId") Long restaurantId);
	
	
}
