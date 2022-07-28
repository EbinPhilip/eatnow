package com.ebin.eatnow.repositories;

import java.util.List;
import java.util.Set;

import com.ebin.eatnow.entities.Item;

public interface ItemRepository {
    public Item findById(String id);

    public List<Item> findById(List<String> ids);

    public List<Item> findByRestaurantId(String id);

    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex);

    public List<Item> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices);

    public boolean existsById(String id);

    public Item create(Item item);

    public Item update(Item item);

    public boolean delete(String restaurantId, int ItemIndex);

}
