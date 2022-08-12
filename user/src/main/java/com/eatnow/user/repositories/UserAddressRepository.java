package com.eatnow.user.repositories;

import java.util.List;

import com.eatnow.user.entities.UserAddressEntity;

public interface UserAddressRepository {

    public List<UserAddressEntity> findByUserId(String userId);

    public UserAddressEntity findByUserIdAndIndex(String userId, int index);

    public boolean existsByUserIdAndIndex(String userId, int index);

    public UserAddressEntity create(UserAddressEntity address);

    public UserAddressEntity update(UserAddressEntity address);

    public boolean delete(String userId, int index);
}
