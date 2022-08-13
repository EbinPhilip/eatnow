package com.eatnow.restaurant.entities;

import java.util.List;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemEntity {

    private int itemIndex;

    @NonNull
    private String name;

    @NonNull
    private Double price;

    private String description;

    private List<String> tags;

    @Default
    boolean available = true;
}
