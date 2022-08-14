package com.eatnow.order.services;

import java.time.LocalDateTime;
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
import com.eatnow.order.dtos.UserAddressDto;
import com.eatnow.order.entities.OrderEntity;
import com.eatnow.order.entities.OrderItem;
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
    public Order createOrder(Order dto) {

        UUID orderId = UUID.randomUUID();

        UserAddressDto address = userAddressService
                .getAddressByUserIdAndIndex(dto.getUserId(), dto.getAddressIndex());

        Map<Integer, OrderItem> items = dto.getItems()
                .stream()
                .map(
                        (i) -> {
                            return OrderItem.builder()
                                    .itemIndex(i.getItemIndex())
                                    .quantity(i.getQuantity())
                                    .build();
                        })
                .collect(Collectors.toMap(
                        (i) -> i.getItemIndex(), (i) -> i));

        List<ItemDto> itemList = menuService
                .checkServiceabilityAndFetchItems(dto.getRestaurantId(),
                        address.getLatitude(),
                        address.getLongitude(),
                        items.keySet());

        double total = 0.0d;
        for (ItemDto itemDto : itemList) {
            OrderItem orderItem = items.get(itemDto.getItemIndex());
            orderItem.setPrice(itemDto.getPrice());
            total = total + orderItem.getPrice() * orderItem.getQuantity();
        }

        OrderEntity order = OrderEntity.builder()
                .id(orderId)
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .address(address.getAddress())
                .items(items.values().stream().collect(Collectors.toList()))
                .total(total)
                .timeStamp(LocalDateTime.now())
                .build();

        cartService.clearCart(order.getUserId());

        order = orderRepository.create(order);
        return dtoFromOrder(order);
    }

    @Transactional
    public Payment confirmOrderAndPay(String orderId) {

        OrderEntity order = orderRepository.findById(UUID.fromString(orderId));
        if (order.getStatus() != OrderEntity.Status.UNPAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot pay for this order.");
        }

        Payment payment = paymentService.pay(orderId, order.getTotal(), "");
        if (payment.getStatus().equals(Payment.PaymentStatus.SUCCESS.toString())) {
            order.setStatus(OrderEntity.Status.NEW);
            order.setTransactionId(UUID.fromString(payment.getTransactionId()));
            orderRepository.update(order);
        } else {
            order.setStatus(OrderEntity.Status.CANCELLED);
        }

        return payment;
    }

    public Order getOrder(String orderId) {

        OrderEntity order = orderRepository.findById(UUID.fromString(orderId));
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

    public List<Order> getOrdersbyUserId(String userId, int page, int size) {

        return orderRepository.findByUserIdPaged(userId, size, page)
                .stream().map(
                        (i) -> {
                            return dtoFromOrder(i);
                        })
                .collect(Collectors.toList());
    }

    @Transactional
    public Order acceptOrder(String orderId) {

        OrderEntity order = orderRepository.findById(UUID.fromString(orderId));
        if (order.getStatus() != OrderEntity.Status.NEW) {
            throw new RuntimeException();
        }
        order.setStatus(OrderEntity.Status.ACCEPTED);
        order = orderRepository.update(order);

        return dtoFromOrder(order);
    }

    @Transactional
    public Order completeOrder(String orderId) {

        OrderEntity order = orderRepository.findById(UUID.fromString(orderId));
        if (order.getStatus() != OrderEntity.Status.ACCEPTED) {
            throw new RuntimeException();
        }
        order.setStatus(OrderEntity.Status.COMPLETED);
        order = orderRepository.update(order);

        return dtoFromOrder(order);
    }

    @Transactional
    public Order cancelOrder(String orderId) {

        OrderEntity order = orderRepository.findById(UUID.fromString(orderId));

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
                .address(order.getAddress())
                .restaurantId(order.getRestaurantId())
                .items(
                        order.getItems().stream().map(
                                (i) -> {
                                    return new Order.Item(i.getItemIndex(),
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
}
