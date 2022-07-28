package com.ebin.eatnow.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebin.eatnow.dtos.OrderDto;
import com.ebin.eatnow.dtos.PaymentDto;
import com.ebin.eatnow.services.OrderService;

@RestController
public class OrderController {
    public static final String orderIdString = "order-id";
    public static final String restaurantIdString = "restaurant-id";
    public static final String userIdString = "user-id";

    public static final String restaurantIdStringParam = "{" + restaurantIdString + "}";
    public static final String userIdStringParam = "{" + userIdString + "}";

    public static final String ORDERS_ENDPOINT = "/orders";
    public static final String ORDER_ID_ENDPOINT_ACCEPT = ORDERS_ENDPOINT + "/accept";
    public static final String ORDER_ID_ENDPOINT_COMPLETE = ORDERS_ENDPOINT + "/complete";
    public static final String ORDER_ID_ENDPOINT_CANCEL = ORDERS_ENDPOINT + "/cancel";

    public static final String ORDER_PAYMENT_ENDPOINT = ORDERS_ENDPOINT + "/payment";

    public static final String ORDERS_RESTAURANT_ENDPOINT = ORDERS_ENDPOINT+"/by-restaurant/" + restaurantIdStringParam;
    public static final String ORDERS_RESTAURANT_ENDPOINT_NEW = ORDERS_RESTAURANT_ENDPOINT + "/new";
    public static final String ORDERS_RESTAURANT_ENDPOINT_ACCEPTED = ORDERS_RESTAURANT_ENDPOINT + "/accepted";
    public static final String ORDERS_RESTAURANT_ENDPOINT_COMPLETED = ORDERS_RESTAURANT_ENDPOINT + "/completed";

    public static final String ORDERS_USER_ENDPOINT = ORDERS_ENDPOINT+"/by-user/" + userIdStringParam;

    @Autowired
    private OrderService orderService;

    @PostMapping(ORDERS_ENDPOINT)
    public ResponseEntity<OrderDto> createOrder(
        @Valid @RequestBody OrderDto order) {
        
        return ResponseEntity.ok().body(
            orderService.createOrder(order));
    }

    @PostMapping(ORDER_PAYMENT_ENDPOINT)
    public ResponseEntity<PaymentDto> payAndConfirmOrder(
        @RequestParam(orderIdString) @NotNull String orderId,
        @RequestParam(name="details", required=false) String paymentDetails) {
        
        return ResponseEntity.ok().body(
            orderService.confirmOrderAndPay(orderId));
    }

    @GetMapping(ORDERS_ENDPOINT)
    public ResponseEntity<OrderDto> fetchOrder(
        @RequestParam(orderIdString) @NotNull String orderId) {
        
        return ResponseEntity.ok().body(
            orderService.getOrder(orderId));
    }

    @PostMapping(ORDER_ID_ENDPOINT_ACCEPT)
    public ResponseEntity<OrderDto> acceptOrder(
        @RequestParam(orderIdString) @NotNull String orderId) {
        
        return ResponseEntity.ok().body(
            orderService.acceptOrder(orderId));
    }

    @PostMapping(ORDER_ID_ENDPOINT_COMPLETE)
    public ResponseEntity<OrderDto> completeOrder(
        @RequestParam(orderIdString) @NotNull String orderId) {
        
        return ResponseEntity.ok().body(
            orderService.completeOrder(orderId));
    }

    @PostMapping(ORDER_ID_ENDPOINT_CANCEL)
    public ResponseEntity<OrderDto> cancelOrder(
        @RequestParam(orderIdString) @NotNull String orderId) {
        
        return ResponseEntity.ok().body(
            orderService.cancelOrder(orderId));
    }

    @GetMapping(ORDERS_RESTAURANT_ENDPOINT)
    public ResponseEntity<List<OrderDto>> fetchOrdersByRestaurantId(
        @PathVariable(restaurantIdString) @NotNull String restaurantId) {
        
        return ResponseEntity.ok().body(
            orderService.getOrdersbyRestaurantId(restaurantId, null));
    }

    @GetMapping(ORDERS_RESTAURANT_ENDPOINT_NEW)
    public ResponseEntity<List<OrderDto>> fetchNewOrdersByRestaurantId(
        @PathVariable(restaurantIdString) @NotNull String restaurantId) {
        
        return ResponseEntity.ok().body(
            orderService.getOrdersbyRestaurantId(restaurantId, "NEW"));
    }

    @GetMapping(ORDERS_RESTAURANT_ENDPOINT_ACCEPTED)
    public ResponseEntity<List<OrderDto>> fetchAcceptedOrdersByRestaurantId(
        @PathVariable(restaurantIdString) @NotNull String restaurantId) {
        
        return ResponseEntity.ok().body(
            orderService.getOrdersbyRestaurantId(restaurantId, "ACCEPTED"));
    }

    @GetMapping(ORDERS_RESTAURANT_ENDPOINT_COMPLETED)
    public ResponseEntity<List<OrderDto>> fetchCompletedOrdersByRestaurantId(
        @PathVariable(restaurantIdString) @NotNull String restaurantId) {
        
        return ResponseEntity.ok().body(
            orderService.getOrdersbyRestaurantId(restaurantId, "COMPLETED"));
    }

    @GetMapping(ORDERS_USER_ENDPOINT)
    public ResponseEntity<List<OrderDto>> fetchOrdersByUserId(
        @PathVariable(userIdString) @NotNull String userId) {
        
        return ResponseEntity.ok().body(
            orderService.getOrdersbyUserId(userId));
    }

}
