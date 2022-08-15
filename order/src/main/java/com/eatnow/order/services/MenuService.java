package com.eatnow.order.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.order.dtos.RestaurantItemsInfo;
import com.eatnow.order.services.clients.MenuRestClient;

@Service
public class MenuService {

    @Autowired
    MenuRestClient menuClient;

    public RestaurantItemsInfo checkServiceabilityAndFetchItems(String restaurantId,
            double latitude, double longitude, Set<Integer> itemIndices) {

        return menuClient.getServiceableItems(restaurantId, latitude, longitude,
                itemIndices);
    }
    
}
