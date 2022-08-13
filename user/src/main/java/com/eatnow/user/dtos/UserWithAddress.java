package com.eatnow.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithAddress {

    @NonNull
    User userDetails;

    @NonNull
    UserAddress address;
}
