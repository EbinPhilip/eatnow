package com.eatnow.order.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
@Builder
public class UserAddress {

    public UserAddress(String userId,
            Integer index, String address, double latitude, double longitude) {

        this.userId = userId;
        this.index = index;
        this.address = address;
        setLatitude(latitude);
        setLongitude(longitude);
    }

    private String userId;

    private Integer index;

    @NonNull
    private String address;

    private double latitude;

    private double longitude;

    public void setLatitude(double latitude) {
        if (latitude < -90.0 || latitude > 90) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "latitude should be between -90.0 and 90.0");
        }
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        if (longitude < -180.0 || longitude > 180) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "longitude should be between -180.0 and 180.0");
        }
        this.longitude = longitude;
    }
}
