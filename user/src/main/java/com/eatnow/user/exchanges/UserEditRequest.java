package com.eatnow.user.exchanges;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditRequest {

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
