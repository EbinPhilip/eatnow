package com.ebin.eatnow.repositories;

import java.util.List;

import com.ebin.eatnow.entities.Item;

public interface ItemRepository {
    Item findById(String id);
    List<Item> findById(List<String> ids);
    List<Item> findByRestaurantId(String id);

    boolean exists(String id);
    Item save(Item item);
}
