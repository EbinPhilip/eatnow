package com.ebin.eatnow.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebin.eatnow.dtos.ItemDto;
import com.ebin.eatnow.dtos.OrderDto;
import com.ebin.eatnow.dtos.PaymentDto;
import com.ebin.eatnow.dtos.UserAddressDto;
import com.ebin.eatnow.entities.Order;
import com.ebin.eatnow.entities.OrderItem;
import com.ebin.eatnow.repositories.OrderRepository;
import com.ebin.eatnow.utils.Location;

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

    @Transactional
    public OrderDto createOrder(OrderDto dto) {

        UUID orderId = UUID.randomUUID();

        UserAddressDto address = userAddressService
                .getAddressByUserIdAndIndex(dto.getUserId(), dto.getAddressIndex());
        Location addressLocation = new Location(address.getLatitude(), address.getLongitude());

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
                        addressLocation,
                        items.keySet());

        double total = 0.0d;
        for (ItemDto itemDto : itemList) {
            OrderItem orderItem = items.get(itemDto.getItemIndex());
            orderItem.setPrice(itemDto.getPrice());
            total = total + orderItem.getPrice() * orderItem.getQuantity();
        }

        Order order = Order.builder()
                .id(orderId)
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .latitude(addressLocation.getLatitude())
                .longitude(addressLocation.getLongitude())
                .address(address.getAddress())
                .items(items.values().stream().collect(Collectors.toList()))
                .total(total)
                .timeStamp(LocalDateTime.now())
                .build();

        order = orderRepository.create(order);
        return dtoFromOrder(order);
    }

    @Transactional
    public PaymentDto confirmOrderAndPay(String orderId) {

        Order order = orderRepository.findById(UUID.fromString(orderId));
        if (order.getStatus() != Order.Status.UNPAID) {
            throw new RuntimeException();
        }

        PaymentDto payment = paymentService.pay(orderId, order.getTotal(), "");
        if (payment.getStatus().equals(PaymentDto.PaymentStatus.SUCCESS.toString())) {
            order.setStatus(Order.Status.NEW);
            order.setTransactionId(UUID.fromString(payment.getTransactionId()));
            orderRepository.update(order);
        } else {
            order.setStatus(Order.Status.CANCELLED);
        }

        return payment;
    }

    public OrderDto getOrder(String orderId) {

        Order order = orderRepository.findById(UUID.fromString(orderId));
        return dtoFromOrder(order);
    }

    public List<OrderDto> getOrdersbyRestaurantId(String restaurantId, String status) {

        List<Order> ordersList;

        if (status == null || status.isEmpty()) {
            ordersList = orderRepository.findByRestaurantId(restaurantId);
        } else {
            ordersList = orderRepository.findByRestaurantIdAndStatus(restaurantId,
                    Order.Status.valueOf(status));
        }

        return ordersList.stream().map(
                (i) -> {
                    return dtoFromOrder(i);
                })
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersbyUserId(String userId) {

        return orderRepository.findByUserId(userId)
                .stream().map(
                        (i) -> {
                            return dtoFromOrder(i);
                        })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto acceptOrder(String orderId) {

        Order order = orderRepository.findById(UUID.fromString(orderId));
        if (order.getStatus() != Order.Status.NEW) {
            throw new RuntimeException();
        }
        order.setStatus(Order.Status.ACCEPTED);
        order = orderRepository.update(order);

        return dtoFromOrder(order);
    }

    @Transactional
    public OrderDto completeOrder(String orderId) {

        Order order = orderRepository.findById(UUID.fromString(orderId));
        if (order.getStatus() != Order.Status.ACCEPTED) {
            throw new RuntimeException();
        }
        order.setStatus(Order.Status.COMPLETED);
        order = orderRepository.update(order);

        return dtoFromOrder(order);
    }

    @Transactional
    public OrderDto cancelOrder(String orderId) {

        Order order = orderRepository.findById(UUID.fromString(orderId));

        if (paymentService.revert(order.getTransactionId().toString())) {
            order.setStatus(Order.Status.CANCELLED);
            order = orderRepository.update(order);
        } else {
            throw new RuntimeException();
        }

        return dtoFromOrder(order);
    }


    private OrderDto dtoFromOrder(Order order) {

        OrderDto dto = OrderDto.builder()
                .orderId(order.getId().toString())
                .userId(order.getUserId())
                .address(order.getAddress())
                .restaurantId(order.getRestaurantId())
                .items(
                        order.getItems().stream().map(
                                (i) -> {
                                    return new OrderDto.Item(i.getItemIndex(),
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
