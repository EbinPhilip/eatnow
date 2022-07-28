package com.ebin.eatnow.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.ebin.eatnow.utils.Location;
import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    public static enum Status
    {
        UNPAID,
        NEW,
        ACCEPTED,
        COMPLETED,
        CANCELLED
    }

    @Id
    private String id;

    @NonNull
    private String userId;

    @NonNull
    private Location addressLocation;
    
    @NonNull
    private String address;

    @NonNull
    private String restaurantId;

    @NonNull
    @Default
    private List<OrderItem> items = new ArrayList<>();

    double total;

    @NonNull
    @Default
    private String transactionId = "";

    @NonNull
    private LocalDateTime timeStamp;

    @Default
    @NonNull
    private Order.Status status = Order.Status.UNPAID;
}
