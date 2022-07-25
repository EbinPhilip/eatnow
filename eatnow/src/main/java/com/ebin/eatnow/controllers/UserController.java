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
import com.ebin.eatnow.services.UserService;

@RestController
public class UserController {
    public static final String USER_ENDPOINT = "/user";
    public static final String USER_API = USER_ENDPOINT+"/{userId}";
    public static final String USER_ADDRESS_API = USER_API + "/address";

    @Autowired
    private UserService userService;

    @GetMapping(USER_API)
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @PostMapping(USER_ENDPOINT)
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody UserDto user) {
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @PutMapping(USER_API)
    public ResponseEntity<UserDto> putUser(
            @PathVariable String userId,
            @Valid @RequestBody UserDto user) {
        
        if (!userId.equals(user.getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                 "User id cannot be changed");
        }
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @GetMapping(USER_ADDRESS_API)
    public ResponseEntity<List<UserAddressDto>> getUserAddresses(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.getAddressByUserId(userId));
    }

    @PostMapping(USER_ADDRESS_API)
    public ResponseEntity<UserAddressDto> postUserAddress(@PathVariable String userId,
    @Valid @RequestBody UserAddressDto address) {
        if (!userId.equals(address.getUserId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                 "User id provided in the address is different");
        }
        return new ResponseEntity<UserAddressDto>(userService.createAddress(address),
                HttpStatus.OK);
    }

    @PutMapping(USER_ADDRESS_API+"/{index}")
    public ResponseEntity<UserAddressDto> putUserAddress(
                @PathVariable("userId") String userId,
                @PathVariable("index") @NotNull Integer index,
                @Valid @RequestBody UserAddressDto address) {

        if (!userId.equals(address.getUserId()) || !index.equals(address.getIndex()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "User id and index cannot be changed");
        }
        return new ResponseEntity<UserAddressDto>(userService.updateAddress(address),
                HttpStatus.OK);
    }

    @DeleteMapping(USER_ADDRESS_API+"/{index}")
    public ResponseEntity<Boolean> deleteUserAddress(
                @PathVariable("userId") String userId,
                @PathVariable("index") @NotNull Integer index) {

        return new ResponseEntity<Boolean>(userService.deleteAddress(userId, index),
                HttpStatus.OK);
    }
}
