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
        setLatitudeInternal(latitude);
        setLongitudeInternal(longitude);
        recalculatePosition();
    }

    public void setLatitude(double latitude)
    {
        setLatitudeInternal(latitude);
        recalculatePosition();
    }

    private void setLatitudeInternal(double latitude)
    {
        if (latitude<-90.0 || latitude>90)
        {
            throw new RuntimeException("Valid latitude values: -90 to 90");
        }
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        setLongitudeInternal(longitude);
        recalculatePosition();
    }

    private void setLongitudeInternal(double longitude)
    {
        if (longitude<-180.0 || longitude>180)
        {
            throw new RuntimeException("Valid longitude values: -180 to 180");
        }
        this.longitude = longitude;
    }

    protected void recalculatePosition()
    {
        this.point = new Point(new Position(latitude, longitude));
    }
}
