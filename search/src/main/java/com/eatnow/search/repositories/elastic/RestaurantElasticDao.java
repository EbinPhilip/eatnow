package com.eatnow.search.repositories.elastic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.eatnow.search.entities.RestaurantEntity;

public interface RestaurantElasticDao extends ElasticsearchRepository<RestaurantEntity,String> {

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
                            "\"isOpen\": \"true\"" +
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
    public Page<RestaurantEntity> findBySearchQueryNear(String query, double distanceInKm,
            double lat, double lon, Pageable pageable);

    @Query(
        "{" +
            "\"bool\": {" +

                "\"filter\": {" +
                    "\"geo_distance\": {" +
                        "\"distance\": \"?0 km\"," +
                        "\"location\": {" +
                            "\"lat\" : ?1," +
                            "\"lon\" : ?2" +
                        "}" +
                    "}" +
                "}" +
                
            "}" +
        "}" )
    public Page<RestaurantEntity> findAllNear(double distanceInKm,
            double lat, double lon, Pageable pageable);
}
