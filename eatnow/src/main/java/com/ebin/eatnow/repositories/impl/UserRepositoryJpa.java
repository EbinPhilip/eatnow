package com.ebin.eatnow.repositories.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.User;
import com.ebin.eatnow.repositories.UserRepository;
import com.ebin.eatnow.repositories.jpaDao.UserJpaDao;

@Repository
@Primary
public class UserRepositoryJpa implements UserRepository {

    @Autowired
    private UserJpaDao dao;

    public User findById(String id) {

        return dao.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<User> findById(List<String> ids) {
        
        return dao.findAllById(ids);
    }

    public boolean existsById(String id) {

        return dao.existsById(id);
    }

    public User create(User user) {

        if (dao.existsById(user.getId())) {

            throw new RuntimeException();
        }
        return dao.save(user);
    }

    public User update(User user) {

        if (!dao.existsById(user.getId())) {

            throw new RuntimeException();
        }

        return dao.save(user);
    }
}
