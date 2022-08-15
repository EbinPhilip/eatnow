package com.eatnow.order.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.order.dtos.ItemDto;
import com.eatnow.order.dtos.Order;
import com.eatnow.order.dtos.Payment;
import com.eatnow.order.dtos.RestaurantItemsInfo;
import com.eatnow.order.dtos.UserWithAddress;
import com.eatnow.order.entities.OrderEntity;
import com.eatnow.order.entities.OrderItem;
import com.eatnow.order.exchanges.OrderRequest;
import com.eatnow.order.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {

        UUID orderId = UUID.randomUUID();

        UserWithAddress userDetails = userAddressService
                .getAddressByUserIdAndIndex(orderRequest.getUserId(), orderRequest.getAddressIndex());

        Map<Integer, OrderRequest.RequestItem> items = orderRequest.getItems()
                .stream()
                .collect(Collectors.toMap(
                        (i) -> i.getItemIndex(), (i) -> i));

        RestaurantItemsInfo itemsInfo = menuService
                .checkServiceabilityAndFetchItems(orderRequest.getRestaurantId(),
                        userDetails.getAddress().getLatitude(),
                        userDetails.getAddress().getLongitude(),
                        items.keySet());

        double total = 0.0d;
        List<OrderItem> orderItems = new ArrayList<>();
        for (ItemDto itemDto : itemsInfo.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .itemIndex(itemDto.getItemIndex())
                    .itemName(itemDto.getName())
                    .price(itemDto.getPrice())
                    .quantity(items.get(itemDto.getItemIndex()).getQuantity())
                    .build();
            orderItems.add(orderItem);
            total = total + orderItem.getPrice() * orderItem.getQuantity();
        }

        OrderEntity order = OrderEntity.builder()
                .id(orderId)
                .userId(orderRequest.getUserId())
                .userName(userDetails.getUserDetails().getName())
                .phone(userDetails.getUserDetails().getPhone())
                .restaurantId(orderRequest.getRestaurantId())
                .restaurantName(itemsInfo.getRestaurantName())
                .latitude(userDetails.getAddress().getLatitude())
                .longitude(userDetails.getAddress().getLongitude())
                .address(userDetails.getAddress().getAddress())
                .items(orderItems)
                .total(total)
                .timeStamp(LocalDateTime.now())
                .build();

        cartService.clearCart(order.getUserId());

        order = orderRepository.create(order);
        return dtoFromOrder(order);
    }

    @Transactional
    public Payment confirmOrderAndPay(String orderId) {

        OrderEntity order = orderRepository.findById(uuidFromString(orderId));
        if (order.getStatus() != OrderEntity.Status.UNPAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot pay for this order.");
        }

        Payment payment = paymentService.pay(orderId, order.getTotal(), "");
        if (payment.getStatus().equals(Payment.PaymentStatus.SUCCESS.toString())) {
            order.setStatus(OrderEntity.Status.NEW);
            order.setTransactionId(uuidFromString(payment.getTransactionId()));
            orderRepository.update(order);
        } else {
            order.setStatus(OrderEntity.Status.CANCELLED);
        }

        return payment;
    }

    public Order getOrder(String orderId) {

        OrderEntity order = orderRepository.findById(uuidFromString(orderId));
        return dtoFromOrder(order);
    }

    public List<Order> getOrdersbyRestaurantId(String restaurantId, String status) {

        List<OrderEntity> ordersList;

        if (status == null || status.isEmpty()) {
            ordersList = orderRepository.findByRestaurantId(restaurantId);
        } else {
            ordersList = orderRepository.findByRestaurantIdAndStatus(restaurantId,
                    OrderEntity.Status.valueOf(status));
        }

        return ordersList.stream().map(
                (i) -> {
                    return dtoFromOrder(i);
                })
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersbyRestaurantId(String restaurantId, int page, int size) {

        List<OrderEntity> ordersList = orderRepository.findByRestaurantIdPaged(restaurantId, size, page);
        return ordersList.stream().map(
                (i) -> {
                    return dtoFromOrder(i);
                })
                .collect(Collectors.toList());
    }


    public List<Order> getOrdersbyUserId(String userId, int page, int size) {

        return orderRepository.findByUserIdPaged(userId, size, page)
                .stream().map(
                        (i) -> {
                            return dtoFromOrder(i);
                        })
                .collect(Collectors.toList());
    }

    @Transactional
    public Order acceptOrder(String orderId, String restaurantId) {

        OrderEntity order = orderRepository.findById(uuidFromString(orderId));
        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (order.getStatus() != OrderEntity.Status.NEW) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        order.setStatus(OrderEntity.Status.ACCEPTED);
        order = orderRepository.update(order);

        return dtoFromOrder(order);
    }

    @Transactional
    public Order completeOrder(String orderId, String restaurantId) {

        OrderEntity order = orderRepository.findById(uuidFromString(orderId));
        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (order.getStatus() != OrderEntity.Status.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        order.setStatus(OrderEntity.Status.COMPLETED);
        order = orderRepository.update(order);

        return dtoFromOrder(order);
    }

    @Transactional
    public Order cancelOrder(String orderId, String restaurantId) {

        OrderEntity order = orderRepository.findById(uuidFromString(orderId));
        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (order.getStatus() != OrderEntity.Status.ACCEPTED ||
            order.getStatus() != OrderEntity.Status.NEW) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (paymentService.revert(order.getTransactionId().toString())) {
            order.setStatus(OrderEntity.Status.CANCELLED);
            order = orderRepository.update(order);
        } else {
            throw new RuntimeException();
        }

        return dtoFromOrder(order);
    }


    private Order dtoFromOrder(OrderEntity order) {

        Order dto = Order.builder()
                .orderId(order.getId().toString())
                .userId(order.getUserId())
                .userName(order.getUserName())
                .phone(order.getPhone())
                .address(order.getAddress())
                .restaurantId(order.getRestaurantId())
                .restaurantName(order.getRestaurantName())
                .items(
                        order.getItems().stream().map(
                                (i) -> {
                                    return new Order.Item(i.getItemName(), i.getItemIndex(),
                                            i.getPrice(), i.getQuantity());
                                }).collect(Collectors.toList()))
                .total(order.getTotal())
                .transactionId(order.getTransactionId() == null ? null
                        : order.getTransactionId().toString())
                .timeStamp(order.getTimeStamp())
                .status(order.getStatus().toString())
                .build();
        return dto;
    }

    private UUID uuidFromString(String uuid) {

        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order-id");
        }
    }
}
