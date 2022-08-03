package com.ebin.eatnow.repositories.jpaDao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebin.eatnow.entities.User;

public interface UserJpaDao extends JpaRepository<User, String> {

}
