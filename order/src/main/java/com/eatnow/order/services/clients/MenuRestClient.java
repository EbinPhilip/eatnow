package com.eatnow.order.services.clients;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatnow.order.dtos.RestaurantItemsInfo;

@FeignClient(value = "menu-client", url = "${menu-service-host}" +
        ":" + "${menu-service-port}")
public interface MenuRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/serviceable-items")
    RestaurantItemsInfo getServiceableItems(
            @RequestParam(name = "restaurant-id") String restaurantId,
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "indices") Set<Integer> itemIndices);
}
