package com.eatnow.user.repositories.jpaDao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatnow.user.entities.UserEntity;

public interface UserJpaDao extends JpaRepository<UserEntity, String> {

}
