package com.ebin.eatnow.repositories.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.Item;
import com.ebin.eatnow.entities.Menu;
import com.ebin.eatnow.repositories.MenuRepository;
import com.ebin.eatnow.repositories.dao.MenuMongoDao;

@Repository
@Primary
public class MenuRepositoryMongo implements MenuRepository {

    @Autowired
    private MenuMongoDao dao;

    public Menu findByRestaurantId(String id) {

        return dao.findByRestaurantId(id)
                .orElseThrow(RuntimeException::new);
    }

    private Menu findMenuByRestaurantIdAndItemIndex(String restaurantId, int itemIndex) {

        return dao.findByRetaurantIdAndItemIndex(restaurantId, itemIndex)
        .orElseThrow(RuntimeException::new);
    }

    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex) {
        
        Menu menu = findMenuByRestaurantIdAndItemIndex(restaurantId, itemIndex);
        
        return menu.getItems().stream()
                .filter((i)->(i.getItemIndex() == itemIndex))
                .findFirst()
                .get();
    }

    public List<Item> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices) {

        Menu menu = findByRestaurantId(restaurantId);

        return menu.getItems()
                .stream()
                .filter(
                    (i)->(indices.contains(i.getItemIndex()))
                ).collect(Collectors.toList());
    }

    public boolean existsByRestaurantId(String id) {

        return dao.existsByRestaurantId(id);
    }

    public Item createItem(String restaurantId, Item item) {

        Menu menu = findByRestaurantId(restaurantId);
        int index = (int) menu.getItems().stream().count() + 1;
        item.setItemIndex(index);
        menu.getItems().add(item);

        dao.save(menu);

        return item;
    }

    public Item updateItem(String restaurantId, int itemIndex, Item item) {

        Menu menu = findMenuByRestaurantIdAndItemIndex(restaurantId, itemIndex);

        menu.getItems().removeIf((i)->(i.getItemIndex() == itemIndex));
        item.setItemIndex(itemIndex);
        menu.getItems().add(item);

        dao.save(menu);

        return item;
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        Menu menu = findMenuByRestaurantIdAndItemIndex(restaurantId, itemIndex);

        menu.getItems().removeIf((i)->(i.getItemIndex() == itemIndex));

        dao.save(menu);

        return true;
    }
}
