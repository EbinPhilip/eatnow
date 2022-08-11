package com.eatnow.search.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "restaurant")
public class Restaurant {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String address;
    
    @NonNull
    private GeoPoint location;

    private String tags;

    @Default
    private boolean isOpen = true;

    @Default
    private double rating = 0;
}
