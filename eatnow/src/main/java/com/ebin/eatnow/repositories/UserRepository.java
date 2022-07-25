package com.ebin.eatnow.repositories;

import java.util.List;

import com.ebin.eatnow.entities.User;

public interface UserRepository {
    public User findById(String id);
    public List<User> findById(List<String> ids);

    public boolean existsById(String id);
    public User create(User user);
    public User update(User user);
}
