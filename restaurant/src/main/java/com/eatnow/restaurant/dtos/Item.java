package com.eatnow.restaurant.dtos;

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
public class Item {

    @Default
    private Integer itemIndex = null;

    @NonNull
    private String name;

    @NonNull
    private Double price;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> tags;

    Boolean available;
}
