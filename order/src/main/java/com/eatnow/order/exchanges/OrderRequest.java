package com.eatnow.order.exchanges;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestItem {

        @NonNull
        @NotNull
        private Integer itemIndex;

        @NonNull
        @NotNull
        private Integer quantity;
    }

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
    @NotEmpty(message = "There are no items in this order")
    @Valid
    List<OrderRequest.RequestItem> items = new ArrayList<>();

}
