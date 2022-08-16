package com.eatnow.user.exchanges;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRequest {

    @NonNull
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull(message = "Latitude cannot be blank")
    private Double latitude;

    @NotNull(message = "Longitude cannot be blank")
    private Double longitude;
}
