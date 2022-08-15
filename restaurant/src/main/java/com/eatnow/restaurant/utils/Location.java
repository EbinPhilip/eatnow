package com.eatnow.restaurant.utils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public Location(GeoJsonPoint point) {

        this(point.getX(), point.getY());
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid latitude values: -90 to 90");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid longitude values: -180 to 180");
        }
        this.longitude = longitude;
    }

    protected void recalculatePosition()
    {
        point = new GeoJsonPoint(latitude, longitude);
    }


    public double getDistanceFrom(Location location) {

        return distance(location.latitude, latitude, location.longitude, longitude, 0, 0);
    }

    private static double distance(
        double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a =
            Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
