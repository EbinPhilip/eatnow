package com.eatnow.order.exchanges;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.eatnow.order.dtos.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {

    @NonNull
    @NotNull
    private String userId;

    @NonNull
    @NotNull
    private Integer addressIndex;

    @NonNull
    @NotNull
    private String restaurantId;

    @NonNull
    @Default
    @NotNull
    List<Order.Item> items = new ArrayList<>();

    @NonNull
    @NotNull
    private Double total;
}
