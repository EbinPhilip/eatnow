package com.ebin.eatnow.repositories;

import java.util.List;

import com.ebin.eatnow.entities.UserAddress;

public interface UserAddressRepository {

    public List<UserAddress> findByUserId(String userId);
    public UserAddress findByUserIdAndIndex(String userId, int index);

    public boolean existsByUserIdAndIndex(String userId, int index);
    public UserAddress create(UserAddress address);
    public UserAddress update(UserAddress address);
    public boolean delete(String userId, int index);
}
