package com.eatnow.user.repositories.dummy;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.eatnow.user.entities.UserEntity;
import com.eatnow.user.repositories.UserRepository;

@Repository
public class UserRepositoryDummy implements UserRepository{
    private Map<String, UserEntity> users;

    public UserRepositoryDummy()
    {
        users = new HashMap<>();

        save(new UserEntity("u1", "u1", "", ""));
        save(new UserEntity("u2", "u2", "", ""));
        save(new UserEntity("u3", "u3", "", ""));
        save(new UserEntity("u4", "u4", "", ""));
    }

    public UserEntity findById(String id)
    {
        return Optional.ofNullable(users.get(id)).orElseThrow(NoSuchElementException::new);
    }

    public boolean existsById(String id)
    {
        return users.containsKey(id);
    }

    public UserEntity create(UserEntity user)
    {
        if (existsById(user.getId()))
        {
            throw new IllegalArgumentException("User id exists");
        }

        return save(user);
    }

    public UserEntity update(UserEntity user)
    {
        if (!existsById(user.getId()))
        {
            throw new NoSuchElementException();
        }

        return save(user);
    }

    private UserEntity save(UserEntity user)
    {
        users.put(user.getId(), user);
        return user;
    }
}
