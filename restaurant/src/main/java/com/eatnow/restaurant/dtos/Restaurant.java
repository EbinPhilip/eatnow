package com.eatnow.restaurant.dtos;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    private String id;

    @NonNull
    private String name;

    private String address;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Default
    private List<String> tags = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Default
    private Double rating = 0.0d;

    @Default
    boolean isOpen = true;
}
