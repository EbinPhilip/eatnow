package com.ebin.eatnow.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Entity
@Table(name="order", schema = "public")
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
    private UUID id;

    @NonNull
    private String userId;
    
    @NonNull
    private String address;

    private double latitude;

    private double longitude;

    @NonNull
    private String restaurantId;

    @NonNull
    @Default
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
    @Column(columnDefinition = "com.vladmihalcea.hibernate.type.json.JsonStringType")
    private List<OrderItem> items = new ArrayList<>();

    double total;

    @Default
    private UUID transactionId = null;

    @NonNull
    private LocalDateTime timeStamp;

    @Default
    @NonNull
    @Enumerated(EnumType.ORDINAL)
    private Order.Status status = Order.Status.UNPAID;
}
