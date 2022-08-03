package com.ebin.eatnow.repositories.mongoDao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ebin.eatnow.entities.Menu;

public interface MenuMongoDao extends MongoRepository<Menu, String> {

    Optional<Menu> findByRestaurantId(String id);

    @Query("{ 'restaurantId' : ?0, 'items.itemIndex' : ?1 }")
    Optional<Menu> findByRetaurantIdAndItemIndex(String id, int index);

    boolean existsByRestaurantId(String id);
}
