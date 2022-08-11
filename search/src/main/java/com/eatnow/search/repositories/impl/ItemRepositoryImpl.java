package com.eatnow.search.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.eatnow.search.entities.Item;
import com.eatnow.search.repositories.ItemRepository;
import com.eatnow.search.repositories.elastic.ItemElasticDao;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @Autowired
    ItemElasticDao itemDao;

    @Override
    public Page<Item> findBySearchQueryNear(String query, double distance,
            double lat, double lon, Pageable pageable) {

        return itemDao.findBySearchQueryNear(query, distance, lat, lon, pageable);
    }
}
