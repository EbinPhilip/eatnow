package com.eatnow.cart.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    
    private String restaurantId;

    private String restaurantName;

    private List<ItemDto> items;
}
