package com.eatnow.order.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.order.dtos.Order;
import com.eatnow.order.dtos.Payment;
import com.eatnow.order.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User APIs")
@SecurityScheme(name = "user token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class UserOrderController {

    public static final String orderIdString = "order-id";
    public static final String userIdString = "user-id";

    public static final String userIdStringParam = "{" + userIdString + "}";

    public static final String ORDERS_ENDPOINT = "/user-orders";
    public static final String ORDER_PAYMENT_ENDPOINT = ORDERS_ENDPOINT + "/payment";
    public static final String USER_ORDERS_ENDPOINT = ORDERS_ENDPOINT + "/" +
            userIdStringParam;

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#order.getUserId() == authentication.principal.username")
    @PostMapping(ORDERS_ENDPOINT)
    @Operation(summary = "Create new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "412", description = "Restaurant or items unavailable, retry later", content = @Content) })
    @SecurityRequirement(name = "user token")
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody Order order) {

        return ResponseEntity.ok().body(
                orderService.createOrder(order));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(ORDER_PAYMENT_ENDPOINT)
    @Operation(summary = "Make payment for order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK") })
    @SecurityRequirement(name = "user token")
    public ResponseEntity<Payment> payAndConfirmOrder(
            @RequestParam(orderIdString) @NotNull String orderId,
            @Parameter(description = "Not used, can be left empty")
            @RequestParam(name = "details", required = false) String paymentDetails) {

        return ResponseEntity.ok().body(
                orderService.confirmOrderAndPay(orderId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostAuthorize("returnObject.getBody().getUserId() == authentication.principal.username")
    @GetMapping(ORDERS_ENDPOINT)
    @Operation(summary = "Fetch order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content) })
    @SecurityRequirement(name = "user token")
    public ResponseEntity<Order> fetchOrder(
            @RequestParam(orderIdString) @NotNull String orderId) {

        return ResponseEntity.ok().body(
                orderService.getOrder(orderId));
    }

    @PreAuthorize("hasRole('ROLE_USER') and" +
            "#userId == authentication.principal.username")
    @GetMapping(USER_ORDERS_ENDPOINT)
    @Operation(summary = "Fetch all orders made by user")
    @SecurityRequirement(name = "user token")
    public ResponseEntity<List<Order>> fetchOrdersByUserId(
            @PathParam(userIdString) @NotNull String userId,
            @RequestParam(value = "page-number", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "20") Integer size) {

        return ResponseEntity.ok().body(
                orderService.getOrdersbyUserId(userId, page, size));
    }

}
