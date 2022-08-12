package com.eatnow.user.repositories;

import com.eatnow.user.entities.UserEntity;

public interface UserRepository {

    public UserEntity findById(String id);

    public boolean existsById(String id);

    public UserEntity create(UserEntity user);

    public UserEntity update(UserEntity user);
}
