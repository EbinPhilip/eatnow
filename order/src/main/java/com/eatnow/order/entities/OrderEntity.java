package com.eatnow.order.entities;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Entity
@Table(name="order", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
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
    private OrderEntity.Status status = OrderEntity.Status.UNPAID;
}
