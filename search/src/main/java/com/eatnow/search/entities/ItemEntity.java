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

@Document(indexName = "item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemEntity {
    @Id
    String id;

    @NonNull
    private String name;

    private int itemIndex;

    @NonNull
    String restaurantId;

    @NonNull
    String restaurantName;

    @NonNull
    private GeoPoint location;

    @NonNull
    private Double price;

    @NonNull
    @Default
    private String description = "";

    @NonNull
    private String tags;

    boolean available;
}
