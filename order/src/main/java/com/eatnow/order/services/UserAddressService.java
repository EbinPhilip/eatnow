package com.eatnow.order.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.order.dtos.UserWithAddress;
import com.eatnow.order.services.clients.AddressRestClient;

@Service
public class UserAddressService {

    @Autowired
    AddressRestClient addressClient;

    public UserWithAddress getAddressByUserIdAndIndex(String userId, int addressIndex) {

        return addressClient.getUserAddressInternal(userId, addressIndex);
    }
}
