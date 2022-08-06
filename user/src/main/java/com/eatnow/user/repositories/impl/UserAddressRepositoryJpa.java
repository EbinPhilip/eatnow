package com.eatnow.user.repositories.impl;

import java.time.Duration;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.eatnow.user.entities.UserAddress;
import com.eatnow.user.repositories.UserAddressRepository;
import com.eatnow.user.repositories.jpaDao.UserAddressJpaDao;
import com.eatnow.user.utils.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Repository
@Primary
public class UserAddressRepositoryJpa implements UserAddressRepository {

    private static final String addressField = "add:";
    private static final Duration userCacheTimeout = Duration
            .ofSeconds(900);

    @Autowired
    UserAddressJpaDao dao;

    private RedisCache cache;

    @Value("${user.redis.host}")
    private String redisHost;

    @Value("${user.redis.port}")
    private int redisPort;

    @PostConstruct
    private void initCache() {

        cache = new RedisCache(redisHost, redisPort);
    }

    public List<UserAddress> findByUserId(String userId) {

        return dao.findByUserId(userId);
    }

    public UserAddress findByUserIdAndIndex(String userId, int index) {
        
        return findByUserIdAndIndexCached(userId, index);
    }

    private UserAddress findByUserIdAndIndexCached(String userId, int index) {

        UserAddress address = null;

        try (Jedis jedis = cache.getResource()) {

            String addressJson = jedis.hget(userId, addressField+String.valueOf(index));
            if (addressJson == null) {

                address = dao.findByUserIdAndIndex(userId, index)
                        .orElseThrow(RuntimeException::new);
                addressJson = toJson(address);
                jedis.hset(userId, addressField+String.valueOf(index), addressJson);
            } else {

                address = fromJson(addressJson);
            }
            jedis.expire(userId, userCacheTimeout.getSeconds());
        }

        return address;
    }

    private UserAddress fromJson(String userJson) {

        try {
            return new ObjectMapper()
                    .readValue(userJson, UserAddress.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(UserAddress user) {

        try {
            return new ObjectMapper()
                    .writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean existsByUserIdAndIndex(String userId, int index) {

        try (Jedis jedis = cache.getResource()) {

            if (jedis.hexists(userId, addressField+String.valueOf(index))) {

                jedis.expire(userId, userCacheTimeout.getSeconds());
                return true;
            }
        }
        return dao.existsByUserIdAndIndex(userId, index);
    }

    private UserAddress save(UserAddress address) {

        address = dao.save(address);

        try(Jedis jedis = cache.getResource()) {

            jedis.hset(address.getUserId(),
                    addressField+String.valueOf(address.getIndex()), toJson(address));
            jedis.expire(address.getUserId(), userCacheTimeout.getSeconds());
        }

        return address;
    }

    public UserAddress create(UserAddress address) {

        return save(address);
    }

    public UserAddress update(UserAddress address) {

        return save(address);
    }

    public boolean delete(String userId, int index) {

        try(Jedis jedis = cache.getResource()) {

            jedis.hdel(userId, addressField+String.valueOf(index));
        }

        return (dao.deleteByUserIdAndIndex(userId, index) == 1);
    }
}
