package com.eatnow.order.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String restaurantId;

    @Default
    private Integer itemIndex = null;

    @NonNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String restaurantName;

    @NonNull
    private Double price;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> tags;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean available;
}
