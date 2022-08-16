package com.eatnow.restaurant.exchanges;

import java.util.List;
import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

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
public class RestaurantEditRequest {

    @NonNull
    @NotBlank
    private String name;

    @Default
    private List<String> tags = new ArrayList<>();
}
