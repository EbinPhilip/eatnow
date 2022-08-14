package com.eatnow.restaurant.repositories.elasticsearch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.eatnow.restaurant.entities.ItemElasticEntity;

public interface ItemElasticRepository extends ElasticsearchRepository<ItemElasticEntity,String>  {
 
    public List<ItemElasticEntity> findByRestaurantId(String restaurantId);

    public Optional<ItemElasticEntity> findByRestaurantIdAndItemIndex(String restaurantId, Integer itemIndex);

    public int deleteByRestaurantIdAndItemIndex(String restaurantId, Integer itemIndex);
}
