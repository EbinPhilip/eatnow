package com.eatnow.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserAddress {
    private String userId;

    private Integer index;

    @NonNull
    private String address;

    private double latitude;

    private double longitude;

    public void setLatitude(double latitude)
    {
        if (latitude<-90.0 || latitude>90)
        {
            throw new RuntimeException("Valid latitude values: -90 to 90");
        }
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        if (longitude<-180.0 || longitude>180)
        {
            throw new RuntimeException("Valid longitude values: -180 to 180");
        }
        this.longitude = longitude;
    }
}
