package com.ebin.eatnow.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

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
}
