package com.ebin.eatnow.repositories;

import java.util.List;

import com.ebin.eatnow.entities.UserAddress;

public interface UserAddressRepository {
    public UserAddress findById(String id);
    public List<UserAddress> findById(List<String> ids);

    public List<UserAddress> findByUserId(String userIds);

    public boolean existsById(String id);
    public UserAddress create(UserAddress address);
    public UserAddress update(UserAddress address);
    public boolean delete(String userId, int index);
}
