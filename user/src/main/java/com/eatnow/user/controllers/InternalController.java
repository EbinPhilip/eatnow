package com.eatnow.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.user.dtos.User;
import com.eatnow.user.dtos.UserAddress;
import com.eatnow.user.dtos.UserWithAddress;
import com.eatnow.user.services.UserAddressService;
import com.eatnow.user.services.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
public class InternalController {

    public static final String INTERNAL_USER_ADDRESS_ENDPOINT = "/internal/user-address";
    public static final String INTERNAL_USER_WITH_ADDRESS_ENDPOINT = "/internal/user-with-address";

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService addressService;

    @Hidden
    @GetMapping(INTERNAL_USER_ADDRESS_ENDPOINT)
    public ResponseEntity<UserAddress> getUserAddressInternal(
            @RequestParam("user-id") String userId,
            @RequestParam("address-index") int index) {

        return new ResponseEntity<UserAddress>(addressService
                .getAddressByUserIdAndIndex(userId, index),
                HttpStatus.OK);
    }

    @Hidden
    @GetMapping(INTERNAL_USER_WITH_ADDRESS_ENDPOINT)
    public ResponseEntity<UserWithAddress> getUserAndAddressInternal(
            @RequestParam("user-id") String userId,
            @RequestParam("address-index") int index) {

        User user = userService.getUserById(userId);
        UserAddress address = addressService.getAddressByUserIdAndIndex(userId, index);

        return new ResponseEntity<UserWithAddress>(
                UserWithAddress.builder()
                        .userDetails(user)
                        .address(address)
                        .build(),
                HttpStatus.OK);
    }
}
