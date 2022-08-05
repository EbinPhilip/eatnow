package com.eatnow.cart.services;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.cart.dtos.ItemDto;
import com.eatnow.cart.dtos.MenuDto;
import com.eatnow.cart.services.clients.MenuRestClient;
import com.eatnow.cart.utils.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Service
public class MenuService {

    private static final String menuCachePrefix = "_menu:";
    private static final Duration cacheTimeout = Duration.ofMinutes(10);

    @Autowired
    MenuRestClient menuClient;

    @Autowired
    RedisCache cache;

    public ItemDto getItemByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        MenuDto menu = null;

        try (Jedis jedis = cache.getResource()) {

            String key = menuCachePrefix + restaurantId;
            String menuJson = jedis.get(key);
            if (menuJson == null) {

                menu = menuClient.getMenu(restaurantId);
                menuJson = toMenuJson(menu);
                jedis.setex(key, cacheTimeout.getSeconds(), menuJson);
            } else {

                menu = fromMenuJson(menuJson);
            }
        }

        return menu.getItems()
                .stream()
                .filter((i)->(i.getItemIndex() == itemIndex))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private MenuDto fromMenuJson(String menuJson) {

        try {
            return new ObjectMapper()
                    .readValue(menuJson, MenuDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toMenuJson(MenuDto menu) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(menu);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
