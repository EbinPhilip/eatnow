package com.eatnow.restaurant.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    
    private String restaurantId;

    private String restaurantName;

    private List<Item> items;
}
