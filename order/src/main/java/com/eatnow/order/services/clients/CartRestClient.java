package com.eatnow.order.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eatnow.order.dtos.Cart;

@FeignClient(value = "cart-client", url = "${cart-service-host}" +
":" + "${cart-service-port}")
public interface CartRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/cart/{user-id}")
    public Cart fetchCart(@PathVariable("user-id")  String userId);

    @RequestMapping(method = RequestMethod.DELETE, value = "/internal/cart/{user-id}")
    public boolean clearCart(@PathVariable("user-id")  String userId);
}
