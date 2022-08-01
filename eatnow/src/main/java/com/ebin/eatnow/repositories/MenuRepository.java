package com.ebin.eatnow.repositories;

import java.util.List;
import java.util.Set;

import com.ebin.eatnow.entities.Item;
import com.ebin.eatnow.entities.Menu;

public interface MenuRepository {

    public Menu findByRestaurantId(String id);

    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex);

    public List<Item> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices);

    public boolean existsByRestaurantId(String id);

    public Item createItem(String restaurantId, Item item);

    public Item updateItem(String restaurantId, int itemIndex, Item item);

    public boolean deleteItem(String restaurantId, int itemIndex);

}
