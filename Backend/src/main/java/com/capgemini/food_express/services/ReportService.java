package com.capgemini.food_express.services;


import java.util.List;
import java.util.Map;

import com.capgemini.food_express.dto.HighestRevenueDTO;
import com.capgemini.food_express.dto.ItemsSoldDTO;
import com.capgemini.food_express.dto.MostOrdersDTO;
import com.capgemini.food_express.dto.RecentOrders;
import com.capgemini.food_express.dto.TopSellingItems;


public interface ReportService {
	List<HighestRevenueDTO> getHighestRevenueReport();
	
	List<MostOrdersDTO> getMostOrdersReport();
	
	List<ItemsSoldDTO> getItemsSoldReport();
	
	List<TopSellingItems> getTopSellingItemsForRestaurant(Long restaurantId);
	
	List<RecentOrders> getRecentOrdersForRestaurant(Long restaurantId);
	
}
