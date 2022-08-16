package com.eatnow.order.controllers;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.order.dtos.Order;
import com.eatnow.order.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Restaurant APIs")
@SecurityScheme(name = "restaurant token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class RestaurantOrderController {

    public static final String orderIdString = "order-id";
    public static final String restaurantIdString = "restaurant-id";
    public static final String userIdString = "user-id";

    public static final String restaurantIdStringParam = "{" + restaurantIdString + "}";
    public static final String userIdStringParam = "{" + userIdString + "}";

    public static final String ORDERS_ENDPOINT = "/restaurant-orders";

    public static final String ORDERS_RESTAURANT_NEW = ORDERS_ENDPOINT + "/new";
    public static final String ORDERS_RESTAURANT_ACCEPTED = ORDERS_ENDPOINT + "/accepted";
    public static final String ORDERS_RESTAURANT_COMPLETED = ORDERS_ENDPOINT + "/completed";
    public static final String ORDERS_RESTAURANT_CANCELLED = ORDERS_ENDPOINT + "/cancelled";

    public static final String ORDERS_RESTAURANT_ENDPOINT = ORDERS_ENDPOINT + "/" + restaurantIdStringParam;

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostAuthorize("returnObject.getBody().getRestaurantId() == authentication.principal.username")
    @GetMapping(ORDERS_ENDPOINT)
    @Operation(summary = "Fetch order by order-id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content) })
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<Order> fetchOrder(
            @RequestParam(orderIdString) @NotNull String orderId) {

        return ResponseEntity.ok().body(
                orderService.getOrder(orderId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @GetMapping(ORDERS_RESTAURANT_NEW)
    @Operation(summary = "Fetch new orders")
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<List<Order>> fetchNewOrdersByRestaurantId(
            @RequestParam(restaurantIdString) @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                orderService.getOrdersbyRestaurantId(restaurantId, "NEW"));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @GetMapping(ORDERS_RESTAURANT_ACCEPTED)
    @Operation(summary = "Fetch accepted orders")
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<List<Order>> fetchAcceptedOrdersByRestaurantId(
            @RequestParam(restaurantIdString) @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                orderService.getOrdersbyRestaurantId(restaurantId, "ACCEPTED"));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping(ORDERS_RESTAURANT_ACCEPTED)
    @Operation(summary = "Move order to accepted status")
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<Order> acceptOrder(
            @RequestParam(orderIdString) @NotNull String orderId) {

        String restaurantId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok().body(
                orderService.acceptOrder(orderId, restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @GetMapping(ORDERS_RESTAURANT_COMPLETED)
    @Operation(summary = "Fetch completed orders")
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<List<Order>> fetchCompletedOrdersByRestaurantId(
            @RequestParam(restaurantIdString) @NotNull String restaurantId) {

        return ResponseEntity.ok().body(
                orderService.getOrdersbyRestaurantId(restaurantId, "COMPLETED"));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping(ORDERS_RESTAURANT_COMPLETED)
    @Operation(summary = "Move order to completed status")
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<Order> completeOrder(
            @RequestParam(orderIdString) @NotNull String orderId) {

        String restaurantId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok().body(
                orderService.completeOrder(orderId, restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping(ORDERS_RESTAURANT_CANCELLED)
    @Operation(summary = "Move order to cancelled status")
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<Order> cancelOrder(
            @RequestParam(orderIdString) @NotNull String orderId) {

        String restaurantId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok().body(
                orderService.cancelOrder(orderId, restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
            "#restaurantId == authentication.principal.username")
    @GetMapping(ORDERS_RESTAURANT_ENDPOINT)
    @Operation(summary = "Fetch orders by restaurant-id")
    @SecurityRequirement(name = "restaurant token")
    public ResponseEntity<List<Order>> fetchOrdersByRestaurantId(
            @PathVariable(restaurantIdString) @NotNull String restaurantId,
            @RequestParam(value = "page-number", required = false) Integer page,
            @RequestParam(value = "page-size", required = false) Integer size) {

        if (page == null || size == null) {
            return ResponseEntity.ok().body(
                    orderService.getOrdersbyRestaurantId(restaurantId, null));
        } else {
            return ResponseEntity.ok().body(
                orderService.getOrdersbyRestaurantId(restaurantId, page, size));
        }
    }

}
