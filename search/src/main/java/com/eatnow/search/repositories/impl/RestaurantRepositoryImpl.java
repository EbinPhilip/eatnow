package com.eatnow.search.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.eatnow.search.entities.Restaurant;
import com.eatnow.search.repositories.RestaurantRepository;
import com.eatnow.search.repositories.elastic.RestaurantElasticDao;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    @Autowired
    RestaurantElasticDao restaurantDao;

    @Override
    public Page<Restaurant> findBySearchQueryNear(String query, double distance,
            double lat, double lon, Pageable pageable) {

        return restaurantDao.findBySearchQueryNear(query, distance, lat, lon, pageable);
    }
}
