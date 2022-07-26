package com.ebin.eatnow.repositories.dummy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.Restaurant;
import com.ebin.eatnow.repositories.RestaurantRepository;
import com.ebin.eatnow.utils.Location;

@Repository
public class RestaurantRepositoryDummy implements RestaurantRepository {
    private HashMap<String, Restaurant> restaurants;

    public RestaurantRepositoryDummy()
    {
        restaurants = new HashMap<>();

        Restaurant r1 = Restaurant.builder()
                            .id("r1")
                            .name("r1")
                            .address("")
                            .location(new Location(11.1, 22.2))
                            .tags(Arrays.asList("food", "yummy"))
                            .rating(4.4)
                            .reviews(10)
                            .build();
        Restaurant r2 = Restaurant.builder()
                            .id("r2")
                            .name("r2")
                            .address("")
                            .location(new Location(11.1, 22.2))
                            .rating(2.4)
                            .reviews(50)
                            .build();
        Restaurant r3 = Restaurant.builder()
                            .id("r3")
                            .name("r3")
                            .address("")
                            .location(new Location(11.1, 22.2))
                            .tags(Arrays.asList(""))
                            .rating(3.4)
                            .reviews(10)
                            .build();
        Restaurant r4 = Restaurant.builder()
                            .id("r4")
                            .name("r4")
                            .address("")
                            .location(new Location(11.1, 22.2))
                            .tags(Arrays.asList("yummy"))
                            .build();
        restaurants.put(r1.getId(), r1);
        restaurants.put(r2.getId(), r2);
        restaurants.put(r3.getId(), r3);
        restaurants.put(r4.getId(), r4);
    }

    @Override
    public Restaurant findById(String id)
    {
        return restaurants.get(id);
    }

    @Override
    public List<Restaurant> findById(List<String> id)
    {
        return restaurants.values().stream()
                .filter((i)->(id.contains(i.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Restaurant> findOpenRestaurantsNearby
        (Location location, double distance)
    {
        return restaurants.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public boolean existsById(String id)
    {
        return restaurants.containsKey(id);
    }

    @Override
    public Restaurant update(Restaurant restaurant)
    {
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }
}
