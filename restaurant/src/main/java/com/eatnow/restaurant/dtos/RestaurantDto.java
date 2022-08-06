package com.eatnow.restaurant.dtos;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

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
public class RestaurantDto {
    private String id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotBlank
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
