package com.eatnow.user.repositories;

import java.util.List;

import com.eatnow.user.entities.UserAddress;

public interface UserAddressRepository {

    public List<UserAddress> findByUserId(String userId);

    public UserAddress findByUserIdAndIndex(String userId, int index);

    public boolean existsByUserIdAndIndex(String userId, int index);

    public UserAddress create(UserAddress address);

    public UserAddress update(UserAddress address);

    public boolean delete(String userId, int index);
}
