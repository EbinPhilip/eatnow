package com.eatnow.user.controllers;

import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.user.dtos.UserAddress;
import com.eatnow.user.exchanges.UserAddressRequest;
import com.eatnow.user.exchanges.UserEditRequest;
import com.eatnow.user.dtos.User;
import com.eatnow.user.services.UserAddressService;
import com.eatnow.user.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User", description = "The user API")
public class UserController {
    public static final String USER_ENDPOINT = "/users";
    public static final String USER_LOGIN_ENDPOINT = USER_ENDPOINT + "/login";
    public static final String USER_API = USER_ENDPOINT + "/{user-id}";
    public static final String USER_ADDRESS_ENDPOINT = USER_API + "/address";
    public static final String USER_ADDRESS_API = USER_ADDRESS_ENDPOINT + "/{address-index}";

    public static final String INTERNAL_USER_ADDRESS_ENDPOINT = "/internal/user-address";

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService addressService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @GetMapping(USER_LOGIN_ENDPOINT)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "user not found")})
    @SecurityRequirements()
    @Operation(summary = "Login to get user token", description = "Login with an existing user-id to get user token. No password required.")
    public ResponseEntity<String> login(@RequestParam("user-id") String userId) {

        User user = userService.getUserById(userId);
        String key = Jwts
                .builder()
                .setIssuer(issuer)
                .setSubject(user.getId())
                .claim("role", "user")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName()))
                .compact();
        return ResponseEntity.ok().body(key);
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @Operation(summary = "Fetch user details", description = "Fetch details of user specified by user-id.")
    @GetMapping(USER_API)
    public ResponseEntity<User> getUser(@PathVariable @NotNull String userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @PostMapping(USER_ENDPOINT)
    @Operation(summary = "Create new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "user created"),
        @ApiResponse(responseCode = "409", description = "user exists already")})
    @SecurityRequirements()
    public ResponseEntity<User> postUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED) .body(userService.createUser(user));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PutMapping(USER_API)
    @Operation(summary = "Update user details", description = "Update details of user specified by user-id.")
    public ResponseEntity<User> putUser(
            @PathVariable @NotNull String userId,
            @Valid @RequestBody UserEditRequest userRequest) {

        User user = User.builder()
                .id(userId)
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .build();
        return ResponseEntity.ok().body(userService.updateUser(userId, user));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @GetMapping(USER_ADDRESS_ENDPOINT)
    @Operation(summary = "Fetch addresses", description = "Fetch all the addresses of the user specified by user-id.")
    public ResponseEntity<List<UserAddress>> getUserAddresses(@PathVariable @NotNull String userId) {
        return ResponseEntity.ok().body(addressService.getAddressesByUserId(userId));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PostMapping(USER_ADDRESS_ENDPOINT)
    @Operation(summary = "Add new address", description = "Add a new address for the user specified by user-id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "address added")})
    @SecurityRequirements()
    public ResponseEntity<UserAddress> postUserAddress(@PathVariable @NotNull String userId,
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
        @ApiResponse(responseCode = "404", description = "address not found")})
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
        @ApiResponse(responseCode = "400", description = "invalid request structure or latitude/longitude values"),
        @ApiResponse(responseCode = "404", description = "address not found")})
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
        @ApiResponse(responseCode = "404", description = "address not found")})
    @Operation(summary = "Delete address", description = "Delete the address specified by user-id and address-index.")
    public ResponseEntity<Boolean> deleteUserAddress(
            @PathVariable("user-id") @NotNull String userId,
            @PathVariable("address-index") @NotNull Integer index) {

        return new ResponseEntity<Boolean>(addressService.deleteAddress(userId, index),
                HttpStatus.OK);
    }

    @Hidden
    @GetMapping(INTERNAL_USER_ADDRESS_ENDPOINT)
    public ResponseEntity<UserAddress> getUserAddressInternal(
            @RequestParam("user-id") String userId,
            @RequestParam("address-index") int index) {

        return new ResponseEntity<UserAddress>(addressService
                .getAddressByUserIdAndIndex(userId, index),
                HttpStatus.OK);
    }
}
