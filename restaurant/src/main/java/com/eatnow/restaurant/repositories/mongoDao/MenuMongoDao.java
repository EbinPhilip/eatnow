package com.eatnow.restaurant.repositories.mongoDao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.eatnow.restaurant.entities.MenuEntity;

public interface MenuMongoDao extends MongoRepository<MenuEntity, String> {

    Optional<MenuEntity> findByRestaurantId(String id);

    @Query("{ 'restaurantId' : ?0, 'items.itemIndex' : ?1 }")
    Optional<MenuEntity> findByRetaurantIdAndItemIndex(String id, int index);

    boolean existsByRestaurantId(String id);
}
