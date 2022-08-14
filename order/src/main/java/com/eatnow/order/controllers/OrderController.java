package com.eatnow.order.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatnow.order.dtos.Order;
import com.eatnow.order.dtos.Payment;
import com.eatnow.order.services.OrderService;

@RestController
public class OrderController {
//     public static final String orderIdString = "order-id";
//     public static final String restaurantIdString = "restaurant-id";
//     public static final String userIdString = "user-id";

//     public static final String restaurantIdStringParam = "{" + restaurantIdString + "}";
//     public static final String userIdStringParam = "{" + userIdString + "}";

//     public static final String ORDERS_ENDPOINT = "/orders";
//     public static final String ORDER_ID_ENDPOINT_ACCEPT = ORDERS_ENDPOINT + "/accept";
//     public static final String ORDER_ID_ENDPOINT_COMPLETE = ORDERS_ENDPOINT + "/complete";
//     public static final String ORDER_ID_ENDPOINT_CANCEL = ORDERS_ENDPOINT + "/cancel";

//     public static final String ORDER_PAYMENT_ENDPOINT = ORDERS_ENDPOINT + "/payment";

//     public static final String ORDERS_RESTAURANT_ENDPOINT = ORDERS_ENDPOINT + "/by-restaurant/"
//             + restaurantIdStringParam;
//     public static final String ORDERS_RESTAURANT_ENDPOINT_NEW = ORDERS_RESTAURANT_ENDPOINT + "/new";
//     public static final String ORDERS_RESTAURANT_ENDPOINT_ACCEPTED = ORDERS_RESTAURANT_ENDPOINT + "/accepted";
//     public static final String ORDERS_RESTAURANT_ENDPOINT_COMPLETED = ORDERS_RESTAURANT_ENDPOINT + "/completed";

//     public static final String ORDERS_USER_ENDPOINT = ORDERS_ENDPOINT + "/by-user/" + userIdStringParam;

//     @Autowired
//     private OrderService orderService;

//     @PreAuthorize("hasRole('ROLE_USER')")
//     @PostAuthorize("returnObject.getBody().getUserId() == authentication.principal.username")
//     @PostMapping(ORDERS_ENDPOINT)
//     public ResponseEntity<Order> createOrder(
//             @Valid @RequestBody Order order) {

//         return ResponseEntity.ok().body(
//                 orderService.createOrder(order));
//     }

//     @PreAuthorize("hasRole('ROLE_USER')")
//     @PostMapping(ORDER_PAYMENT_ENDPOINT)
//     public ResponseEntity<Payment> payAndConfirmOrder(
//             @RequestParam(orderIdString) @NotNull String orderId,
//             @RequestParam(name = "details", required = false) String paymentDetails) {

//         return ResponseEntity.ok().body(
//                 orderService.confirmOrderAndPay(orderId));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT') or hasRole('ROLE_USER')")
//     @PostAuthorize("returnObject.getBody().getRestaurantId() == authentication.principal.username" +
//             " or returnObject.getBody().getUserId() == authentication.principal.username")
//     @GetMapping(ORDERS_ENDPOINT)
//     public ResponseEntity<Order> fetchOrder(
//             @RequestParam(orderIdString) @NotNull String orderId) {

//         return ResponseEntity.ok().body(
//                 orderService.getOrder(orderId));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT')")
//     @PostAuthorize("returnObject.getBody().getRestaurantId()" +
//             " == authentication.principal.username")
//     @PostMapping(ORDER_ID_ENDPOINT_ACCEPT)
//     public ResponseEntity<Order> acceptOrder(
//             @RequestParam(orderIdString) @NotNull String orderId) {

//         return ResponseEntity.ok().body(
//                 orderService.acceptOrder(orderId));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT')")
//     @PostAuthorize("returnObject.getBody().getRestaurantId()" +
//             " == authentication.principal.username")
//     @PostMapping(ORDER_ID_ENDPOINT_COMPLETE)
//     public ResponseEntity<Order> completeOrder(
//             @RequestParam(orderIdString) @NotNull String orderId) {

//         return ResponseEntity.ok().body(
//                 orderService.completeOrder(orderId));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT')")
//     @PostAuthorize("returnObject.getBody().getRestaurantId()" +
//             " == authentication.principal.username")
//     @PostMapping(ORDER_ID_ENDPOINT_CANCEL)
//     public ResponseEntity<Order> cancelOrder(
//             @RequestParam(orderIdString) @NotNull String orderId) {

//         return ResponseEntity.ok().body(
//                 orderService.cancelOrder(orderId));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
//             "#restaurantId == authentication.principal.username")
//     @GetMapping(ORDERS_RESTAURANT_ENDPOINT)
//     public ResponseEntity<List<Order>> fetchOrdersByRestaurantId(
//             @PathVariable(restaurantIdString) @NotNull String restaurantId) {

//         return ResponseEntity.ok().body(
//                 orderService.getOrdersbyRestaurantId(restaurantId, null));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
//             "#restaurantId == authentication.principal.username")
//     @GetMapping(ORDERS_RESTAURANT_ENDPOINT_NEW)
//     public ResponseEntity<List<Order>> fetchNewOrdersByRestaurantId(
//             @PathVariable(restaurantIdString) @NotNull String restaurantId) {

//         return ResponseEntity.ok().body(
//                 orderService.getOrdersbyRestaurantId(restaurantId, "NEW"));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
//             "#restaurantId == authentication.principal.username")
//     @GetMapping(ORDERS_RESTAURANT_ENDPOINT_ACCEPTED)
//     public ResponseEntity<List<Order>> fetchAcceptedOrdersByRestaurantId(
//             @PathVariable(restaurantIdString) @NotNull String restaurantId) {

//         return ResponseEntity.ok().body(
//                 orderService.getOrdersbyRestaurantId(restaurantId, "ACCEPTED"));
//     }

//     @PreAuthorize("hasRole('ROLE_RESTAURANT') and" +
//             "#restaurantId == authentication.principal.username")
//     @GetMapping(ORDERS_RESTAURANT_ENDPOINT_COMPLETED)
//     public ResponseEntity<List<Order>> fetchCompletedOrdersByRestaurantId(
//             @PathVariable(restaurantIdString) @NotNull String restaurantId) {

//         return ResponseEntity.ok().body(
//                 orderService.getOrdersbyRestaurantId(restaurantId, "COMPLETED"));
//     }

//     @PreAuthorize("hasRole('ROLE_USER') and" +
//             "#userId == authentication.principal.username")
//     @GetMapping(ORDERS_USER_ENDPOINT)
//     public ResponseEntity<List<Order>> fetchOrdersByUserId(
//             @PathVariable(userIdString) @NotNull String userId) {

//         return ResponseEntity.ok().body(
//                 orderService.getOrdersbyUserId(userId, 0, 20));
//     }

}
