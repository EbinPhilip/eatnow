package com.ebin.eatnow.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Menu {
    @Id
    String id;

    String restaurantId;

    String restaurantName;

    List<Item> items = new ArrayList<>();
}
