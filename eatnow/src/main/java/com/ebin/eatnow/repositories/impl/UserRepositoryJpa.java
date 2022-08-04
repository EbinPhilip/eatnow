package com.ebin.eatnow.repositories.impl;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.User;
import com.ebin.eatnow.repositories.UserRepository;
import com.ebin.eatnow.repositories.jpaDao.UserJpaDao;
import com.ebin.eatnow.utils.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Repository
@Primary
public class UserRepositoryJpa implements UserRepository {

    private static final String userField = "profile";
    private static final Duration userCacheTimeout = Duration
            .ofSeconds(900);

    @Autowired
    private UserJpaDao dao;

    private RedisCache cache;

    @Value("${user.redis.host}")
    private String redisHost;

    @Value("${user.redis.port}")
    private int redisPort;

    @PostConstruct
    private void initCache() {

        cache = new RedisCache(redisHost, redisPort);
    }

    public User findById(String id) {

        return findByIdCached(id);
    }

    private User findByIdCached(String id) {

        User user = null;

        try(Jedis jedis = cache.getResource()) {
    
            String userJson = jedis.hget(id, userField);
            if (userJson == null) {

                user = dao.findById(id)
                    .orElseThrow(RuntimeException::new);
                userJson = toJson(user);
                jedis.hset(id, userField, userJson);
            } else {

                user = fromJson(userJson);    
            }
            jedis.expire(id, userCacheTimeout.getSeconds());
        }

        return user;
    }

    private User fromJson(String userJson) {

        try {
            return new ObjectMapper()
                    .readValue(userJson, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(User user) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean existsById(String id) {

        try (Jedis jedis = cache.getResource()) {

            if (jedis.exists(id)) {
                jedis.expire(id, userCacheTimeout.getSeconds());
                return true;
            }
        }
        return dao.existsById(id);
    }

    public User create(User user) {

        if (existsById(user.getId())) {

            throw new RuntimeException();
        }

        try (Jedis jedis = cache.getResource()) {

            jedis.hset(user.getId(), userField, toJson(user));
            jedis.expire(user.getId(), userCacheTimeout.getSeconds());
        }

        return dao.save(user);
    }

    public User update(User user) {

        try (Jedis jedis = cache.getResource()) {

            jedis.hset(user.getId(), userField, toJson(user));
            jedis.expire(user.getId(), userCacheTimeout.getSeconds());
        }

        return dao.save(user);
    }
}
