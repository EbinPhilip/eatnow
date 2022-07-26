package com.ebin.eatnow.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ebin.eatnow.dtos.UserAddressDto;
import com.ebin.eatnow.dtos.UserDto;
import com.ebin.eatnow.services.UserAddressService;
import com.ebin.eatnow.services.UserService;

@RestController
public class UserController {
    public static final String USER_ENDPOINT = "/users";
    public static final String USER_API = USER_ENDPOINT+"/{userId}";
    public static final String USER_ADDRESS_ENDPOINT = USER_API + "/address";
    public static final String USER_ADDRESS_API = USER_ADDRESS_ENDPOINT + "/{index}";

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService addressService;

    @GetMapping(USER_API)
    public ResponseEntity<UserDto> getUser(@PathVariable @NotNull String userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @PostMapping(USER_ENDPOINT)
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody UserDto user) {
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @PutMapping(USER_API)
    public ResponseEntity<UserDto> putUser(
            @PathVariable @NotNull String userId,
            @Valid @RequestBody UserDto user) {
        
        if (!userId.equals(user.getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                 "User id cannot be changed");
        }
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @GetMapping(USER_ADDRESS_ENDPOINT)
    public ResponseEntity<List<UserAddressDto>> getUserAddresses(@PathVariable @NotNull String userId) {
        return ResponseEntity.ok().body(addressService.getAddressesByUserId(userId));
    }

    @PostMapping(USER_ADDRESS_ENDPOINT)
    public ResponseEntity<UserAddressDto> postUserAddress(@PathVariable @NotNull String userId,
    @Valid @RequestBody UserAddressDto address) {
        if (!userId.equals(address.getUserId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                 "User id provided in the address is different");
        }
        return new ResponseEntity<UserAddressDto>(addressService.createAddress(address),
                HttpStatus.OK);
    }

    @GetMapping(USER_ADDRESS_API)
    public ResponseEntity<UserAddressDto> getUserAddressesByIndex(
        @PathVariable("userId") @NotNull String userId,
        @PathVariable("index") Integer index) {

        return ResponseEntity.ok().body(addressService.getAddressByUserIdAndIndex(userId, index));
    }

    @PutMapping(USER_ADDRESS_API)
    public ResponseEntity<UserAddressDto> putUserAddress(
                @PathVariable("userId") @NotNull String userId,
                @PathVariable("index") @NotNull Integer index,
                @Valid @RequestBody UserAddressDto address) {

        if (!userId.equals(address.getUserId()) || !index.equals(address.getIndex()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "User id and address index cannot be changed");
        }
        return new ResponseEntity<UserAddressDto>(addressService.updateAddress(address),
                HttpStatus.OK);
    }

    @DeleteMapping(USER_ADDRESS_API)
    public ResponseEntity<Boolean> deleteUserAddress(
                @PathVariable("userId") @NotNull String userId,
                @PathVariable("index") @NotNull Integer index) {

        return new ResponseEntity<Boolean>(addressService.deleteAddress(userId, index),
                HttpStatus.OK);
    }
}
