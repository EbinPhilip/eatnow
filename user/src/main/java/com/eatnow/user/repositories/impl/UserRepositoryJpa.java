package com.eatnow.user.repositories.impl;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.user.entities.UserEntity;
import com.eatnow.user.repositories.UserRepository;
import com.eatnow.user.repositories.jpaDao.UserJpaDao;
import com.eatnow.user.utils.RedisCache;
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

    public UserEntity findById(String id) {

        return findByIdCached(id);
    }

    private UserEntity findByIdCached(String id) {

        UserEntity user = null;

        try (Jedis jedis = cache.getResource()) {

            String userJson = jedis.hget(id, userField);
            if (userJson == null) {

                user = dao.findById(id).orElseThrow(
                        () -> (new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("User with user-id:%s not found", id))));
                userJson = toJson(user);
                jedis.hset(id, userField, userJson);
            } else {

                user = fromJson(userJson);
            }
            jedis.expire(id, userCacheTimeout.getSeconds());
        }

        return user;
    }

    private UserEntity fromJson(String userJson) {

        try {
            return new ObjectMapper()
                    .readValue(userJson, UserEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(UserEntity user) {

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

    public UserEntity create(UserEntity user) {

        if (existsById(user.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("User with id:%s already exists", user.getId()));
        }

        try (Jedis jedis = cache.getResource()) {

            jedis.hset(user.getId(), userField, toJson(user));
            jedis.expire(user.getId(), userCacheTimeout.getSeconds());
        }

        return dao.save(user);
    }

    public UserEntity update(UserEntity user) {

        try (Jedis jedis = cache.getResource()) {

            jedis.hset(user.getId(), userField, toJson(user));
            jedis.expire(user.getId(), userCacheTimeout.getSeconds());
        }

        return dao.save(user);
    }
}
