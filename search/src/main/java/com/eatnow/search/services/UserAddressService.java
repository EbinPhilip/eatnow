package com.eatnow.search.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.search.dtos.UserAddress;
import com.eatnow.search.services.clients.AddressRestClient;

@Service
public class UserAddressService {

    @Autowired
    private AddressRestClient addressClient;

    public UserAddress getAddressByUserIdAndIndex(String userId, int addressIndex) {

        return addressClient.getUserAddressInternal(userId, addressIndex);
    }
}
