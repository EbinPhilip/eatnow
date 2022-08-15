package com.eatnow.order.repositories.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;

import com.eatnow.order.entities.OrderEntity;
import com.eatnow.order.repositories.OrderRepository;
import com.eatnow.order.repositories.jpaDao.OrderJpaDao;
import com.eatnow.order.utils.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@Repository
@Primary
public class OrderRepositoryJpa implements OrderRepository {

    private static final String newStatus = ":new";
    private static final String acceptedStatus = ":acc";

    @Autowired
    OrderJpaDao dao;

    private RedisCache cache;

    @Value("${order.redis.host}")
    private String redisHost;

    @Value("${order.redis.port}")
    private int redisPort;

    @PostConstruct
    private void initCache() {

        cache = new RedisCache(redisHost, redisPort);
    }

    public OrderEntity findById(UUID orderId) {

        return findByIdCached(orderId);
    }

    private OrderEntity findByIdCached(UUID orderId) {

        OrderEntity order = null;

        try(Jedis jedis = cache.getResource()) {

            String orderJson = jedis.get(orderId.toString());
            if (orderJson != null) {
    
                order = fromJson(orderJson);
            } else {
    
                order = dao.findById(orderId)
                        .orElseThrow(RuntimeException::new);
            }
        }

        return order;
    }

    private OrderEntity fromJson(String orderJson) {

        try {
            return new ObjectMapper()
                    .findAndRegisterModules()
                    .readValue(orderJson, OrderEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toJson(OrderEntity order) {

        try {
            return new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(order);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<OrderEntity> findByUserId(String userId) {

        Pageable page = PageRequest.of(0, 10);
        return dao.findByUserIdOrderByTimeStampDesc(userId, page).getContent();
    }

    public List<OrderEntity> findByUserIdPaged(String userId, int pageSize, int pageNumber) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        return dao.findByUserIdOrderByTimeStampDesc(userId, page).getContent();
    }

    public List<OrderEntity> findByRestaurantId(String restaurantId) {

        LocalDateTime stamp = LocalDateTime.now().minusDays(1);
        return dao.findByRestaurantIdAndTimeStampGreaterThan(restaurantId, stamp);
    }

    public List<OrderEntity> findByRestaurantIdPaged(String restaurantId, int pageSize, int pageNumber) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        return dao.findByRestaurantIdOrderByTimeStampDesc(restaurantId, page).getContent();
    }

    public List<OrderEntity> findByRestaurantIdAndStatus(String restaurantId, OrderEntity.Status status) {

        if (status == OrderEntity.Status.NEW || status == OrderEntity.Status.ACCEPTED) {

            try (Jedis jedis = cache.getResource()) {

                Set<String> orderIds = null;
                if (status == OrderEntity.Status.NEW) {
                    orderIds = jedis.smembers(restaurantId + newStatus);
                } else {
                    orderIds = jedis.smembers(restaurantId + acceptedStatus);
                }

                if (!orderIds.isEmpty()) {

                    List<String> orderJsons = jedis.mget(orderIds.toArray(new String[0]));
                    return orderJsons.stream()
                            .map((i)->{
                                return fromJson(i);
                            })
                            .collect(Collectors.toList());
                }
            }            
        }
        
        return findByRestaurantId(restaurantId)
                .stream()
                .filter((i)->(i.getStatus() == status))
                .collect(Collectors.toList());
    }

    public boolean existsById(UUID orderId) {

        try (Jedis jedis = cache.getResource()) {

            if (jedis.exists(orderId.toString())) {

                return true;
            }
        }

        return dao.existsById(orderId);
    }

    public OrderEntity create(OrderEntity order) {

        try (Jedis jedis = cache.getResource()) {

            jedis.set(order.getId().toString(), toJson(order));
        }

        return dao.save(order);
    }

    public OrderEntity update(OrderEntity order) {

        if (order.getStatus() == OrderEntity.Status.NEW) {
    
            try (Jedis jedis = cache.getResource()) {

                jedis.set(order.getId().toString(), toJson(order));
                jedis.sadd(order.getRestaurantId() + newStatus, order.getId().toString());
            }
        } else if (order.getStatus() == OrderEntity.Status.ACCEPTED) {
    
            try (Jedis jedis = cache.getResource()) {

                jedis.set(order.getId().toString(), toJson(order));
                jedis.srem(order.getRestaurantId() + newStatus, order.getId().toString());
                jedis.sadd(order.getRestaurantId() + acceptedStatus, order.getId().toString());
            }
        } else if (order.getStatus() == OrderEntity.Status.COMPLETED
                || order.getStatus() == OrderEntity.Status.CANCELLED) {
    
            try (Jedis jedis = cache.getResource()) {

                jedis.srem(order.getRestaurantId() + newStatus, order.getId().toString());
                jedis.srem(order.getRestaurantId() + acceptedStatus, order.getId().toString());
                jedis.del(order.getId().toString());
            }
        } else { //UNPAID

            try (Jedis jedis = cache.getResource()) {

                jedis.set(order.getId().toString(), toJson(order));
            }
        }

        return dao.save(order);
    }
}
