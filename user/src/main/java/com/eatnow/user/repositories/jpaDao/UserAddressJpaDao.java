package com.eatnow.user.repositories.jpaDao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatnow.user.entities.UserAddressEntity;

public interface UserAddressJpaDao extends JpaRepository<UserAddressEntity, String> {

    public List<UserAddressEntity> findByUserId(String userId);

    public Optional<UserAddressEntity> findByUserIdAndIndex(String userId, int index);

    public boolean existsByUserIdAndIndex(String userId, int index);

    public int deleteByUserIdAndIndex(String userId, int index);
}
