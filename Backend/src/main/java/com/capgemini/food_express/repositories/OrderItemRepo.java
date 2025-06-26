package com.capgemini.food_express.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capgemini.food_express.dto.ItemsSoldDTO;
import com.capgemini.food_express.entities.OrderItem;
import java.util.List;


public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{
	List<OrderItem> findByOrderId(Long orderId);
	
	List<OrderItem> findByOrderIdIn(List<Long> orderIds);
	
	@Query(value = """
		    SELECT r.name, r.location, SUM(oi.quantity) AS itemsSold
		    FROM order_item oi
		    JOIN orders o ON oi.order_id = o.id
		    JOIN restaurant r ON o.restaurant_id = r.id
		    GROUP BY r.id, r.name, r.location
		    ORDER BY itemsSold DESC
		    """, nativeQuery = true)
		List<Object[]> getItemsSoldReportRaw();
		 @Query(value = """
			        SELECT 
			            f.id AS id,
			            f.name AS name,
			            f.category AS category,
			            f.price AS price,
			            SUM(oi.quantity) AS qty
			        FROM orders o
			        JOIN order_item oi ON o.id = oi.order_id
			        JOIN food_item f ON f.id = oi.item_id
			        WHERE o.restaurant_id = :restaurantId
			        GROUP BY f.id, f.name, f.category, f.price
			        ORDER BY qty DESC
			        LIMIT 5
			    """, nativeQuery = true)
			    List<Object[]> findTopSellingItemsNative(@Param("restaurantId") Long restaurantId);
}
