package com.eatnow.user.repositories;

import com.eatnow.user.entities.User;

public interface UserRepository {

    public User findById(String id);

    public boolean existsById(String id);

    public User create(User user);

    public User update(User user);
}
