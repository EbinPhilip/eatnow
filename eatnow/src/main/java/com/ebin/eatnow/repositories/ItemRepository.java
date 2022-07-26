package com.ebin.eatnow.repositories;

import java.util.List;

import com.ebin.eatnow.entities.Item;

public interface ItemRepository {
    public Item findById(String id);
    public List<Item> findById(List<String> ids);

    public List<Item> findByRestaurantId(String id);
    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex);

    public boolean existsById(String id);
    public Item create(Item item);
    public Item update(Item item);
    public boolean delete(String restaurantId, int ItemIndex);

}
