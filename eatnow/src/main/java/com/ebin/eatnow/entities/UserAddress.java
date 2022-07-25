package com.ebin.eatnow.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ebin.eatnow.utils.Location;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Builder.Default;

@Data
@Builder
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Default
    private String id = null;

    @NonNull
    private String userId;

    @Default
    private Integer index = null;

    @NonNull
    private String address;

    @NonNull
    private Location location;
}
