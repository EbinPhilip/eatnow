package com.ebin.eatnow.utils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import lombok.Data;
import lombok.NonNull;

@Data
public class Location {
    @NonNull
    @Max(90)
    @Min(-90)
    @JsonIgnore
    private Double latitude;

    @NonNull
    @Max(180)
    @Min(-180)
    @JsonIgnore
    private Double longitude;
    
    @JsonUnwrapped
    private Point point;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        point = new Point(new Position(latitude, longitude));
    }

}
