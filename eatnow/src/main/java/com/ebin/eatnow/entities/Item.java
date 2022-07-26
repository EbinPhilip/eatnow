package com.ebin.eatnow.entities;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Default
    private String id = null;

    @NonNull
    private String restaurantId;

    @Default
    private Integer itemIndex = null;

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

    @Default
    boolean available = true;
}
