package com.eatnow.user.repositories.jpaDao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatnow.user.entities.UserAddress;

public interface UserAddressJpaDao extends JpaRepository<UserAddress, String> {

    public List<UserAddress> findByUserId(String userId);

    public Optional<UserAddress> findByUserIdAndIndex(String userId, int index);

    public boolean existsByUserIdAndIndex(String userId, int index);

    public int deleteByUserIdAndIndex(String userId, int index);
}
