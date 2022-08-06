package com.eatnow.user.repositories.jpaDao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatnow.user.entities.User;

public interface UserJpaDao extends JpaRepository<User, String> {

}
