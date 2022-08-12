package com.eatnow.search.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eatnow.search.entities.RestaurantEntity;

public interface RestaurantRepository {

    public Page<RestaurantEntity> findBySearchQueryNear(String query, double distance,
            double lat, double lon, Pageable pageable);

    public Page<RestaurantEntity> findAllNear(double distanceInKm,
            double lat, double lon, Pageable pageable);
}
