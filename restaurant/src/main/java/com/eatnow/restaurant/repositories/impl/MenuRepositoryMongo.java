package com.eatnow.restaurant.repositories.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.eatnow.restaurant.entities.Item;
import com.eatnow.restaurant.entities.Menu;
import com.eatnow.restaurant.repositories.MenuRepository;
import com.eatnow.restaurant.repositories.mongoDao.MenuMongoDao;
import com.eatnow.restaurant.utils.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Repository
@Primary
public class MenuRepositoryMongo implements MenuRepository {

    private static final String menuPrefix = "m";

    @Autowired
    private MenuMongoDao dao;

    @Autowired
    private RedisCache cache;

    public Menu findByRestaurantId(String id) {

        return findByRestaurantIdFromCache(id);
    }

    private Menu findByRestaurantIdFromDb(String restaurantId) {

        return dao.findByRestaurantId(restaurantId)
                .orElseThrow(RuntimeException::new);
    }

    private Menu findByRestaurantIdFromCache(String restaurantId) {

        Menu menu = null;

        try (Jedis jedis = cache.getResource()) {

            String menuJson = jedis.hget(restaurantId, menuPrefix);
            if (menuJson == null) {

                menu = findByRestaurantIdFromDb(restaurantId);
                menuJson = toJson(menu);
                jedis.hset(restaurantId, menuPrefix, menuJson);
            } else {

                menu = fromJson(menuJson);
            }
        }

        return menu;
    }

    private Menu fromJson(String menuJson) {

        try {
            return new ObjectMapper()
                    .readValue(menuJson, Menu.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(Menu menu) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(menu);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        Menu menu = findByRestaurantIdFromCache(restaurantId);
        return menu.getItems().stream()
                .filter((i) -> (i.getItemIndex() == itemIndex))
                .findFirst()
                .get();
    }

    public List<Item> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices) {

        Menu menu = findByRestaurantIdFromCache(restaurantId);
        return menu.getItems()
                .stream()
                .filter(
                        (i) -> (indices.contains(i.getItemIndex())))
                .collect(Collectors.toList());
    }

    public boolean existsByRestaurantId(String id) {

        try (Jedis jedis = cache.getResource()) {

            if (jedis.exists(id)) {
                return true;
            }
        }
        return dao.existsByRestaurantId(id);
    }

    private Menu saveMenu(Menu menu) {

        try (Jedis jedis = cache.getResource()) {
            jedis.hset(menu.getRestaurantId(), menuPrefix, toJson(menu));
        }

        return dao.save(menu);
    }

    public Item createItem(String restaurantId, Item item) {

        Menu menu = findByRestaurantIdFromCache(restaurantId);

        int index = Collections.max(menu.getItems(), new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                return Integer.valueOf(i1.getItemIndex())
                        .compareTo(i2.getItemIndex());
            }
        }).getItemIndex() + 1;

        item.setItemIndex(index);
        menu.getItems().add(item);

        saveMenu(menu);

        return item;
    }

    private Integer findListPositionOfItem(Menu menu, int itemIndex) {

        int index = 0;
        for (Item elem : menu.getItems()) {

            if (elem.getItemIndex() == itemIndex) {

                return index;
            }
            ++index;
        }

        return null;
    }

    public Item updateItem(String restaurantId, Item item) {

        Menu menu = findByRestaurantIdFromCache(restaurantId);
        int index = Optional.ofNullable(findListPositionOfItem(menu, item.getItemIndex()))
                .orElseThrow(RuntimeException::new);

        menu.getItems().set(index, item);
        saveMenu(menu);

        return item;
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        Menu menu = findByRestaurantIdFromCache(restaurantId);
        int index = Optional.ofNullable(findListPositionOfItem(menu, itemIndex))
                .orElseThrow(RuntimeException::new);

        menu.getItems().remove(index);
        saveMenu(menu);

        return true;
    }
}
