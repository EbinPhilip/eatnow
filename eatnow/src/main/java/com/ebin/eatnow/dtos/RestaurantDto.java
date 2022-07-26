package com.ebin.eatnow.dtos;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    @NonNull
    @NotBlank
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
}
