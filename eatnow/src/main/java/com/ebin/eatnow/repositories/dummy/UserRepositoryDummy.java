package com.ebin.eatnow.repositories.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.User;
import com.ebin.eatnow.repositories.UserRepository;

@Repository
public class UserRepositoryDummy implements UserRepository{
    private Map<String, User> users;

    public UserRepositoryDummy()
    {
        users = new HashMap<>();

        save(new User("u1", "u1", "", ""));
        save(new User("u2", "u2", "", ""));
        save(new User("u3", "u3", "", ""));
        save(new User("u4", "u4", "", ""));
    }

    public User findById(String id)
    {
        return Optional.ofNullable(users.get(id)).orElseThrow(NoSuchElementException::new);
    }

    public List<User> findById(List<String> ids)
    {
        List<User> usersList = new ArrayList<>();
        usersList.addAll(
            users.values().stream().filter(
                (i)->(ids.contains(i.getId()))
            ).collect(Collectors.toList())
        );

        return usersList;
    }

    public boolean existsById(String id)
    {
        return users.containsKey(id);
    }

    public User create(User user)
    {
        if (existsById(user.getId()))
        {
            throw new IllegalArgumentException("User id exists");
        }

        return save(user);
    }

    public User update(User user)
    {
        if (!existsById(user.getId()))
        {
            throw new NoSuchElementException();
        }

        return save(user);
    }

    private User save(User user)
    {
        users.put(user.getId(), user);
        return user;
    }
}
