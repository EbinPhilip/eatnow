package com.eatnow.cart.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eatnow.cart.dtos.MenuDto;

@FeignClient(value = "menu-client", url = "${menu-service-host}" +
        ":" + "${menu-service-port}")
public interface MenuRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/menu/{restaurantId}")
    MenuDto getMenu(@PathVariable("restaurantId") String restaurantId);
}
