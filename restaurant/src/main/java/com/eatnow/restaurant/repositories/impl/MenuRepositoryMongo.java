package com.eatnow.restaurant.repositories.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.restaurant.entities.ItemEntity;
import com.eatnow.restaurant.entities.MenuEntity;
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

    public MenuEntity findByRestaurantId(String id) {

        return findByRestaurantIdFromCache(id);
    }

    private MenuEntity findByRestaurantIdFromDb(String restaurantId) {

        return dao.findByRestaurantId(restaurantId)
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "restaurant not found")));
    }

    private MenuEntity findByRestaurantIdFromCache(String restaurantId) {

        MenuEntity menu = null;

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

    private MenuEntity fromJson(String menuJson) {

        try {
            return new ObjectMapper()
                    .readValue(menuJson, MenuEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(MenuEntity menu) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(menu);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public ItemEntity findByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        MenuEntity menu = findByRestaurantIdFromCache(restaurantId);
        return menu.getItems().stream()
                .filter((i) -> (i.getItemIndex() == itemIndex))
                .findFirst()
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "item not found")));
    }

    public List<ItemEntity> findByRestaurantIdAndIndices(String restaurantId,
            Set<Integer> indices) {

        MenuEntity menu = findByRestaurantIdFromCache(restaurantId);
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

    private MenuEntity saveMenu(MenuEntity menu) {

        try (Jedis jedis = cache.getResource()) {
            jedis.hset(menu.getRestaurantId(), menuPrefix, toJson(menu));
        }

        return dao.save(menu);
    }

    public ItemEntity createItem(String restaurantId, ItemEntity item) {

        MenuEntity menu = findByRestaurantIdFromCache(restaurantId);

        int index = Collections.max(menu.getItems(), new Comparator<ItemEntity>() {
            @Override
            public int compare(ItemEntity i1, ItemEntity i2) {
                return Integer.valueOf(i1.getItemIndex())
                        .compareTo(i2.getItemIndex());
            }
        }).getItemIndex() + 1;

        item.setItemIndex(index);
        menu.getItems().add(item);

        saveMenu(menu);

        return item;
    }

    private Integer findListPositionOfItem(MenuEntity menu, int itemIndex) {

        int index = 0;
        for (ItemEntity elem : menu.getItems()) {

            if (elem.getItemIndex() == itemIndex) {

                return index;
            }
            ++index;
        }

        return null;
    }

    public ItemEntity updateItem(String restaurantId, ItemEntity item) {

        MenuEntity menu = findByRestaurantIdFromCache(restaurantId);
        int index = Optional.ofNullable(findListPositionOfItem(menu, item.getItemIndex()))
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "item not found")));

        menu.getItems().set(index, item);
        saveMenu(menu);

        return item;
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        MenuEntity menu = findByRestaurantIdFromCache(restaurantId);
        int index = Optional.ofNullable(findListPositionOfItem(menu, itemIndex))
                .orElseThrow(() -> (new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "item not found")));

        menu.getItems().remove(index);
        saveMenu(menu);

        return true;
    }
}
