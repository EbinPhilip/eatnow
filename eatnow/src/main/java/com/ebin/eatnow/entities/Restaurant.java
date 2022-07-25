package com.ebin.eatnow.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.ebin.eatnow.utils.Location;

import lombok.Data;
import lombok.NonNull;

@Entity
@Data
public class Restaurant {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String address;

    @NonNull
    private Location location;

    private List<String> tags = new ArrayList<>();

    private boolean isOpen = false;

    private double rating = 0;

    private int reviews = 0;
}
