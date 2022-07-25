package com.ebin.eatnow.entities;

import java.util.List;

import javax.persistence.Id;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Data
public class Item {
    @Id
    private String id;

    @NonNull
    private String restaurantId;

    @NonNull
    private String name;

    @NonNull
    private String restaurantName;
    
    @NonNull
    private String category;

    @NonNull
    private Double price;

    private String description;

    private List<String> tags;
}
