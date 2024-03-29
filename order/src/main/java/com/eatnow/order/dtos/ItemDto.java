package com.eatnow.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {;

    @NonNull
    private Integer itemIndex;

    @NonNull
    private String name;

    @NonNull
    private Double price;
}
