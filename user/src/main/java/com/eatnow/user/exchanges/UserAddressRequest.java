package com.eatnow.user.exchanges;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRequest {

    @NonNull
    @NotBlank
    private String address;

    @NotBlank
    private double latitude;

    @NotBlank
    private double longitude;
}
