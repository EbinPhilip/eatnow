package com.eatnow.search.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eatnow.search.entities.Item;

public interface ItemRepository {

    public Page<Item> findBySearchQueryNear(String query, double distance,
            double lat, double lon, Pageable pageable);
}
