package com.eatnow.restaurant.repositories.dummy;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eatnow.restaurant.entities.ItemEntity;
import com.eatnow.restaurant.entities.MenuEntity;
import com.eatnow.restaurant.repositories.MenuRepository;

@Repository
public class MenuRepositoryDummy implements MenuRepository {

    private HashMap<String, MenuEntity> menus;

    public MenuRepositoryDummy() {
        menus = new HashMap<>();

        ItemEntity i1 = ItemEntity.builder()
                .itemIndex(1)
                .name("i1")
                .price(100.0)
                .build();
        ItemEntity i2 = ItemEntity.builder()
                .itemIndex(2)
                .name("i2")
                .price(100.0)
                .build();
        ItemEntity i3 = ItemEntity.builder()
                .itemIndex(1)
                .name("i3")
                .price(100.0)
                .build();
        ItemEntity i4 = ItemEntity.builder()
                .itemIndex(2)
                .name("i4")
                .price(100.0)
                .build();

        MenuEntity r1 = new MenuEntity();
        r1.setRestaurantId("r1");
        r1.setRestaurantName("r1");
        r1.getItems().add(i1);
        r1.getItems().add(i2);

        MenuEntity r2 = new MenuEntity();
        r2.setRestaurantId("r2");
        r2.setRestaurantName("r2");
        r2.getItems().add(i3);
        r2.getItems().add(i4);

        menus.put(r1.getRestaurantId(), r1);
        menus.put(r2.getRestaurantId(), r2);
    }

    public MenuEntity findByRestaurantId(String restaurantId) {

        return Optional.ofNullable(menus.get(restaurantId))
                        .orElseThrow(RuntimeException::new);
    }

    public ItemEntity findByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        List<ItemEntity> items = findByRestaurantId(restaurantId).getItems();
        ItemEntity item = items.stream().filter(
                (i) -> (i.getItemIndex() == itemIndex))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        return item;
    }

    public List<ItemEntity> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices) {
        
        List<ItemEntity> items = findByRestaurantId(restaurantId).getItems();
        return items.stream().filter(
                (i) -> (indices.contains(i.getItemIndex())))
                .collect(Collectors.toList());
    }

    public boolean existsByRestaurantId(String id) {
        return menus.containsKey(id);
    }

    public ItemEntity createItem(String restaurantId, ItemEntity item) {

        MenuEntity menu = findByRestaurantId(restaurantId);
        int index = (int)menu.getItems().stream().count() + 1;
        item.setItemIndex(index);

        menu.getItems().add(item);

        return item;
    }

    public ItemEntity updateItem(String restaurantId, ItemEntity item) {

        MenuEntity menu = findByRestaurantId(restaurantId);
        menu.getItems().stream()
                        .filter((i)->(i.getItemIndex() == item.getItemIndex()))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);
        
        List<ItemEntity> items = menu.getItems()
                        .stream()
                        .filter((i)->(i.getItemIndex() != item.getItemIndex()))
                        .collect(Collectors.toList());

        items.add(item);

        menu.setItems(items);

        return item;
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        MenuEntity menu = findByRestaurantId(restaurantId);
        List<ItemEntity> items = menu.getItems()
                        .stream()
                        .filter((i)->(i.getItemIndex() != itemIndex))
                        .collect(Collectors.toList());
        
        menu.setItems(items);
        return true;
    }
}
