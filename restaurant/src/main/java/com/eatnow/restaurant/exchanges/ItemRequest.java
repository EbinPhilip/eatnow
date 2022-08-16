package com.eatnow.restaurant.exchanges;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class ItemRequest {

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotNull
    private Double price;

    @Default
    private String description = "";

    @Default
    private List<String> tags = new ArrayList<>();

    @NonNull
    @NotNull
    Boolean available;
}
