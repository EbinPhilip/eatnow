package com.eatnow.search.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatnow.search.dtos.UserAddressDto;

@FeignClient(value = "address-client", url = "${user-service-host}" +
":" + "${user-service-port}")
public interface AddressRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/internal/user-address")
    public UserAddressDto getUserAddressInternal(
            @RequestParam("user-id") String userId,
            @RequestParam("address-index") int index);
}
