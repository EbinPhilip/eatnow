package com.eatnow.restaurant.repositories;

import java.util.List;
import java.util.Set;

import com.eatnow.restaurant.entities.ItemEntity;
import com.eatnow.restaurant.entities.MenuEntity;

public interface MenuRepository {

    public MenuEntity findByRestaurantId(String id);

    public ItemEntity findByRestaurantIdAndIndex(String restaurantId, int itemIndex);

    public List<ItemEntity> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices);

    public boolean existsByRestaurantId(String id);

    public ItemEntity createItem(String restaurantId, ItemEntity item);

    public ItemEntity updateItem(String restaurantId, ItemEntity item);

    public boolean deleteItem(String restaurantId, int itemIndex);

}
