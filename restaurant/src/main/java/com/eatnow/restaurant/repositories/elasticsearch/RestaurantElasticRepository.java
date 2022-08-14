package com.eatnow.restaurant.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.eatnow.restaurant.entities.RestaurantElasticEntity;

public interface RestaurantElasticRepository extends ElasticsearchRepository<RestaurantElasticEntity,String> {

}
