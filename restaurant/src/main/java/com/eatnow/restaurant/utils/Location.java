package com.eatnow.restaurant.utils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.hsr.geohash.GeoHash;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Location {
    @NonNull
    @Max(90)
    @Min(-90)
    private Double latitude;

    @NonNull
    @Max(180)
    @Min(-180)
    private Double longitude;
    
    @JsonIgnore
    private GeoJsonPoint point;

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

    public GeoHash getGeoHash() {
        return GeoHash.withCharacterPrecision(latitude,
            longitude, 7);
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
        point = new GeoJsonPoint(latitude, longitude);
    }
}
