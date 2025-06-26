package com.capgemini.food_express.services;


import java.security.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.food_express.dto.HighestRevenueDTO;
import com.capgemini.food_express.dto.ItemsSoldDTO;
import com.capgemini.food_express.dto.MostOrdersDTO;
import com.capgemini.food_express.dto.RecentOrders;
import com.capgemini.food_express.dto.TopSellingItems;
import com.capgemini.food_express.entities.Order;
import com.capgemini.food_express.entities.OrderItem;
import com.capgemini.food_express.entities.Restaurant;
import com.capgemini.food_express.entities.User;
import com.capgemini.food_express.repositories.FoodItemRepo;
import com.capgemini.food_express.repositories.OrderItemRepo;
import com.capgemini.food_express.repositories.OrderRepo;
import com.capgemini.food_express.repositories.RestaurantRepo;
import com.capgemini.food_express.repositories.UserRepository;

@Service
public class ReportServiceImpl implements ReportService{
	final private OrderRepo orderRepo;
    final private UserRepository userRepo;
    final private RestaurantRepo restaurantRepo;
    final private OrderItemRepo orderItemRepo;
    final private FoodItemRepo foodItemRepo;
    
    
    

    public ReportServiceImpl(OrderRepo orderRepo, UserRepository userRepo, RestaurantRepo restaurantRepo,
			OrderItemRepo orderItemRepo,FoodItemRepo foodItemRepo) {
		super();
		this.orderRepo = orderRepo;
		this.userRepo = userRepo;
		this.restaurantRepo = restaurantRepo;
		this.orderItemRepo = orderItemRepo;
		this.foodItemRepo=foodItemRepo;
	}

	public List<HighestRevenueDTO> getHighestRevenueReport() {
		return orderRepo.getHighestRevenueReport();
    }

    public List<MostOrdersDTO> getMostOrdersReport() {
    	 List<Object[]> rawData = orderRepo.getMostOrdersReportRaw();

    	    return rawData.stream()
    	        .map(row -> new MostOrdersDTO(
    	            (String) row[0],        // u.name
    	            (String) row[1],        // u.email
    	            ((Number) row[2]).longValue() // COUNT(o.id)
    	        ))
    	        .collect(Collectors.toList());
    }

    public List<ItemsSoldDTO> getItemsSoldReport() {
    	List<Object[]> rawData = orderItemRepo.getItemsSoldReportRaw();

        return rawData.stream()
            .map(row -> new ItemsSoldDTO(
                (String) row[0],        // r.name
                (String) row[1],        // r.location
                ((Number) row[2]).longValue() // SUM(oi.quantity)
            ))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TopSellingItems> getTopSellingItemsForRestaurant(Long restaurantId) {
    	List<Object[]> rawItems = orderItemRepo.findTopSellingItemsNative(restaurantId);
    	List<TopSellingItems> result = new ArrayList<>();
    	 for (Object[] row : rawItems) {
    	TopSellingItems dto = new TopSellingItems(
                ((Number) row[0]).longValue(),
                (String) row[1],
                (String) row[2],
                ((Number) row[3]).doubleValue(),
                ((Number) row[4]).intValue()
            );
            result.add(dto);
    }
        return result;
    }
    
    
    @Override
    public List<RecentOrders> getRecentOrdersForRestaurant(Long restaurantId) {
    	List<Object[]> rows = orderRepo.findRecentOrdersWithItemCount(restaurantId);
        List<RecentOrders> result = new ArrayList<>();

        for (Object[] row : rows) {
            RecentOrders dto = new RecentOrders(
                ((Number) row[0]).longValue(),
                ((java.sql.Date) row[1]).toLocalDate().atStartOfDay(),
                ((Number) row[2]).doubleValue(),
                (String) row[3],
                ((Number) row[4]).intValue()
            );
            result.add(dto);
        }

        

        return result;
    }

	
}
