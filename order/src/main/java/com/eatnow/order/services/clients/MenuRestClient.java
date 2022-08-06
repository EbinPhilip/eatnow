package com.eatnow.order.services.clients;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatnow.order.dtos.ItemDto;

@FeignClient(value = "menu-client", url = "localhost:8081")
public interface MenuRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/serviceable-items")
    List<ItemDto> getServiceableItems(
            @RequestParam(name = "restaurant-id") String restaurantId,
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "indices") Set<Integer> itemIndices);
}
