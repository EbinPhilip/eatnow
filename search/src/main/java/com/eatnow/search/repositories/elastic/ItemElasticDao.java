package com.eatnow.search.repositories.elastic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.eatnow.search.entities.ItemEntity;

public interface ItemElasticDao extends ElasticsearchRepository<ItemEntity,String>  {

    @Query(
        "{" +
            "\"bool\": {" +

                "\"must\": [" +
                    "{" +
                        "\"match\": {" +
                            "\"name.ngrams\": \"?0\"" +
                        "}" +
                    "}," +
                    "{" +
                        "\"term\": {" +
                            "\"available\": \"true\"" +
                        "}" +
                    "}" +
                "]," +

                "\"should\": [" +
                    "{" +
                        "\"match\": {" +
                            "\"name\": \"?0\"" +
                        "}" +
                    "}," +
                    "{" +
                        "\"match\": {" +
                            "\"restaurantName.ngrams\": \"?0\"" +
                        "}" +
                    "}," +
                    "{" +
                        "\"match\": {" +
                            "\"tags\": \"?0\"" +
                        "}" +
                    "}" +
                "]," +

                "\"filter\": {" +
                    "\"geo_distance\": {" +
                        "\"distance\": \"?1 km\"," +
                        "\"location\": {" +
                            "\"lat\" : ?2," +
                            "\"lon\" : ?3" +
                        "}" +
                    "}" +
                "}" +
                
            "}" +
        "}" )
    public Page<ItemEntity> findBySearchQueryNear(String query, double distanceInKm,
            double lat, double lon, Pageable pageable);
}
