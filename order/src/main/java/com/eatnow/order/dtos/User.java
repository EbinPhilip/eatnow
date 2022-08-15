package com.eatnow.order.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @NonNull
    @NotBlank(message = "User ID cannot be blank")
    private String id;

    @NonNull
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NonNull
    @NotBlank(message = "Phone number cannot be blank")
    private String phone;

    @NonNull
    @NotBlank(message = "Email cannot be blank")
    private String email;
}
