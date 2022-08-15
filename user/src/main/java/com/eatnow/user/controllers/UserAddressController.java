package com.eatnow.user.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.user.dtos.UserAddress;
import com.eatnow.user.exchanges.UserAddressRequest;
import com.eatnow.user.services.UserAddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User Address APIs")
public class UserAddressController {

    public static final String USER_ADDRESS_ENDPOINT = "users/{user-id}/address";
    public static final String USER_ADDRESS_API = USER_ADDRESS_ENDPOINT + "/{address-index}";

    @Autowired
    private UserAddressService addressService;

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @GetMapping(USER_ADDRESS_ENDPOINT)
    @Operation(summary = "Fetch addresses", description = "Fetch all the addresses of the user specified by user-id.")
    public ResponseEntity<List<UserAddress>> getUserAddresses(
        @PathVariable("user-id") @NotNull String userId) {
    
        return ResponseEntity.ok().body(addressService.getAddressesByUserId(userId));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PostMapping(USER_ADDRESS_ENDPOINT)
    @Operation(summary = "Add new address", description = "Add a new address for the user specified by user-id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "address added")})
    public ResponseEntity<UserAddress> postUserAddress(@PathVariable("user-id") @NotNull String userId,
            @Valid @RequestBody UserAddressRequest addressRequest) {

        UserAddress address = UserAddress.builder()
                .address(addressRequest.getAddress())
                .latitude(addressRequest.getLatitude())
                .longitude(addressRequest.getLongitude())
                .build();
        return new ResponseEntity<UserAddress>(addressService.createAddress(userId, address),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @GetMapping(USER_ADDRESS_API)
    @Operation(summary = "Fetch address", description = "Fetch the address specified by user-id and address-index.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "address not found", content = @Content)})
    public ResponseEntity<UserAddress> getUserAddressesByIndex(
            @PathVariable("user-id") @NotNull String userId,
            @PathVariable("address-index") Integer index) {

        return ResponseEntity.ok().body(addressService.getAddressByUserIdAndIndex(userId, index));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PutMapping(USER_ADDRESS_API)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "invalid request structure or latitude/longitude values", content = @Content),
        @ApiResponse(responseCode = "404", description = "address not found", content = @Content)})
    @Operation(summary = "Update address", description = "Update the address specified by user-id and address-index.")
    public ResponseEntity<UserAddress> putUserAddress(
            @PathVariable("user-id") @NotNull String userId,
            @PathVariable("address-index") @NotNull Integer index,
            @Valid @RequestBody UserAddressRequest addressRequest) {

        UserAddress address = UserAddress.builder()
                .address(addressRequest.getAddress())
                .latitude(addressRequest.getLatitude())
                .longitude(addressRequest.getLongitude())
                .build();
        return new ResponseEntity<UserAddress>(addressService.updateAddress(userId, index, address),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @DeleteMapping(USER_ADDRESS_API)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "address not found", content = @Content)})
    @Operation(summary = "Delete address", description = "Delete the address specified by user-id and address-index.")
    public ResponseEntity<Boolean> deleteUserAddress(
            @PathVariable("user-id") @NotNull String userId,
            @PathVariable("address-index") @NotNull Integer index) {

        return new ResponseEntity<Boolean>(addressService.deleteAddress(userId, index),
                HttpStatus.OK);
    }
}
