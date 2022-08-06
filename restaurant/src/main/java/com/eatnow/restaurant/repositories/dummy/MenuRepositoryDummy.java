package com.eatnow.restaurant.repositories.dummy;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eatnow.restaurant.entities.Item;
import com.eatnow.restaurant.entities.Menu;
import com.eatnow.restaurant.repositories.MenuRepository;

@Repository
public class MenuRepositoryDummy implements MenuRepository {

    private HashMap<String, Menu> menus;

    public MenuRepositoryDummy() {
        menus = new HashMap<>();

        Item i1 = Item.builder()
                .itemIndex(1)
                .name("i1")
                .price(100.0)
                .build();
        Item i2 = Item.builder()
                .itemIndex(2)
                .name("i2")
                .price(100.0)
                .build();
        Item i3 = Item.builder()
                .itemIndex(1)
                .name("i3")
                .price(100.0)
                .build();
        Item i4 = Item.builder()
                .itemIndex(2)
                .name("i4")
                .price(100.0)
                .build();

        Menu r1 = new Menu();
        r1.setRestaurantId("r1");
        r1.setRestaurantName("r1");
        r1.getItems().add(i1);
        r1.getItems().add(i2);

        Menu r2 = new Menu();
        r2.setRestaurantId("r2");
        r2.setRestaurantName("r2");
        r2.getItems().add(i3);
        r2.getItems().add(i4);

        menus.put(r1.getRestaurantId(), r1);
        menus.put(r2.getRestaurantId(), r2);
    }

    public Menu findByRestaurantId(String restaurantId) {

        return Optional.ofNullable(menus.get(restaurantId))
                        .orElseThrow(RuntimeException::new);
    }

    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        List<Item> items = findByRestaurantId(restaurantId).getItems();
        Item item = items.stream().filter(
                (i) -> (i.getItemIndex() == itemIndex))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        return item;
    }

    public List<Item> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices) {
        
        List<Item> items = findByRestaurantId(restaurantId).getItems();
        return items.stream().filter(
                (i) -> (indices.contains(i.getItemIndex())))
                .collect(Collectors.toList());
    }

    public boolean existsByRestaurantId(String id) {
        return menus.containsKey(id);
    }

    public Item createItem(String restaurantId, Item item) {

        Menu menu = findByRestaurantId(restaurantId);
        int index = (int)menu.getItems().stream().count() + 1;
        item.setItemIndex(index);

        menu.getItems().add(item);

        return item;
    }

    public Item updateItem(String restaurantId, Item item) {

        Menu menu = findByRestaurantId(restaurantId);
        menu.getItems().stream()
                        .filter((i)->(i.getItemIndex() == item.getItemIndex()))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);
        
        List<Item> items = menu.getItems()
                        .stream()
                        .filter((i)->(i.getItemIndex() != item.getItemIndex()))
                        .collect(Collectors.toList());

        items.add(item);

        menu.setItems(items);

        return item;
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        Menu menu = findByRestaurantId(restaurantId);
        List<Item> items = menu.getItems()
                        .stream()
                        .filter((i)->(i.getItemIndex() != itemIndex))
                        .collect(Collectors.toList());
        
        menu.setItems(items);
        return true;
    }
}
