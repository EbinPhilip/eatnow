package com.eatnow.order.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatnow.order.dtos.UserWithAddress;

@FeignClient(value = "address-client", url = "${user-service-host}" +
":" + "${user-service-port}")
public interface AddressRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/user-with-address")
    public UserWithAddress getUserAddressInternal(
            @RequestParam("user-id") String userId,
            @RequestParam("address-index") int index);
}
