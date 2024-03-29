package com.ebin.eatnow.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name="user", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
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
