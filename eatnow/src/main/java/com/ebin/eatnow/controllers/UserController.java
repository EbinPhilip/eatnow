package com.ebin.eatnow.controllers;

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

import com.ebin.eatnow.dtos.UserAddressDto;
import com.ebin.eatnow.dtos.UserDto;
import com.ebin.eatnow.services.UserAddressService;
import com.ebin.eatnow.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {
    public static final String USER_ENDPOINT = "/users";
    public static final String USER_LOGIN_ENDPOINT = USER_ENDPOINT + "/login";
    public static final String USER_API = USER_ENDPOINT + "/{userId}";
    public static final String USER_ADDRESS_ENDPOINT = USER_API + "/address";
    public static final String USER_ADDRESS_API = USER_ADDRESS_ENDPOINT + "/{index}";

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService addressService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @GetMapping(USER_LOGIN_ENDPOINT)
    public ResponseEntity<String> login(@RequestParam("user-id") String userId) {

        UserDto user = userService.getUserById(userId);
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
    @GetMapping(USER_API)
    public ResponseEntity<UserDto> getUser(@PathVariable @NotNull String userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @PostMapping(USER_ENDPOINT)
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody UserDto user) {
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PutMapping(USER_API)
    public ResponseEntity<UserDto> putUser(
            @PathVariable @NotNull String userId,
            @Valid @RequestBody UserDto user) {

        return ResponseEntity.ok().body(userService.updateUser(userId, user));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @GetMapping(USER_ADDRESS_ENDPOINT)
    public ResponseEntity<List<UserAddressDto>> getUserAddresses(@PathVariable @NotNull String userId) {
        return ResponseEntity.ok().body(addressService.getAddressesByUserId(userId));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PostMapping(USER_ADDRESS_ENDPOINT)
    public ResponseEntity<UserAddressDto> postUserAddress(@PathVariable @NotNull String userId,
            @Valid @RequestBody UserAddressDto address) {

        return new ResponseEntity<UserAddressDto>(addressService.createAddress(userId, address),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @GetMapping(USER_ADDRESS_API)
    public ResponseEntity<UserAddressDto> getUserAddressesByIndex(
            @PathVariable("userId") @NotNull String userId,
            @PathVariable("index") Integer index) {

        return ResponseEntity.ok().body(addressService.getAddressByUserIdAndIndex(userId, index));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @PutMapping(USER_ADDRESS_API)
    public ResponseEntity<UserAddressDto> putUserAddress(
            @PathVariable("userId") @NotNull String userId,
            @PathVariable("index") @NotNull Integer index,
            @Valid @RequestBody UserAddressDto address) {

        return new ResponseEntity<UserAddressDto>(addressService.updateAddress(userId, index, address),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @DeleteMapping(USER_ADDRESS_API)
    public ResponseEntity<Boolean> deleteUserAddress(
            @PathVariable("userId") @NotNull String userId,
            @PathVariable("index") @NotNull Integer index) {

        return new ResponseEntity<Boolean>(addressService.deleteAddress(userId, index),
                HttpStatus.OK);
    }
}
