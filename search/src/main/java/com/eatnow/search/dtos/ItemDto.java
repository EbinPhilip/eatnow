package com.eatnow.search.dtos;

import java.util.ArrayList;
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

    @NonNull
    private String name;

    private int itemIndex;

    @NonNull
    String restaurantId;

    @NonNull
    String restaurantName;

    @NonNull
    private Double price;

    @Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description = "";

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Default
    private List<String> tags = new ArrayList<>();
}