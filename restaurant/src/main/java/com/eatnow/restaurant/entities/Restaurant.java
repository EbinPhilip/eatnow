package com.eatnow.restaurant.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Restaurant {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String address;
    
    @NonNull
    private GeoJsonPoint location;

    @Default
    private List<String> tags = new ArrayList<>();

    @Default
    private boolean isOpen = true;

    @Default
    private double rating = 0;

    @Default
    private int reviews = 0;

    @SuppressWarnings("unchecked")
    @JsonProperty("location")
    private void setLocationFromJson(Map<String, Object> geoPoint) {

        List<Double> points = (List<Double>)geoPoint.get("coordinates");
        this.location = new GeoJsonPoint(points.get(0), points.get(1));
    }
}
