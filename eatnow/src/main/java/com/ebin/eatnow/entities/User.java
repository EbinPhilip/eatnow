package com.ebin.eatnow.entities;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Entity
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String phone;

    @NonNull
    private String email;
}
