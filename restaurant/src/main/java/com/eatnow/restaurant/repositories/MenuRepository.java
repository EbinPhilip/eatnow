package com.eatnow.restaurant.repositories;

import java.util.List;
import java.util.Set;

import com.eatnow.restaurant.entities.Item;
import com.eatnow.restaurant.entities.Menu;

public interface MenuRepository {

    public Menu findByRestaurantId(String id);

    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex);

    public List<Item> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices);

    public boolean existsByRestaurantId(String id);

    public Item createItem(String restaurantId, Item item);

    public Item updateItem(String restaurantId, Item item);

    public boolean deleteItem(String restaurantId, int itemIndex);

}
