package com.eatnow.search.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eatnow.search.entities.ItemEntity;

public interface ItemRepository {

    public Page<ItemEntity> findBySearchQueryNear(String query, double distance,
            double lat, double lon, Pageable pageable);
}
