package com.ebin.eatnow.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.ebin.eatnow.utils.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Entity
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
    private Location location;

    @Default
    private List<String> tags = new ArrayList<>();

    @Default
    private boolean isOpen = true;

    @Default
    private double rating = 0;

    @Default
    private int reviews = 0;
}
