package com.eatnow.user.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.eatnow.user.dtos.User;
import com.eatnow.user.entities.UserEntity;
import com.eatnow.user.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User getUserById(String userId) {

        UserEntity user = repository.findById(userId);
        return userToDto(user);
    }

    @Transactional
    public User createUser(User dto) {

        UserEntity user = dtoToUser(dto);
        repository.create(user);
        return userToDto(user);
    }

    @Transactional
    public User updateUser(String userId, User dto) {

        dto.setId(userId);
        UserEntity user = dtoToUser(dto);
        repository.update(user);
        return userToDto(user);
    }

    private UserEntity dtoToUser(User dto) {

        UserEntity user = UserEntity.builder()
            .id(dto.getId())
            .name(dto.getName())
            .phone(dto.getPhone())
            .email(dto.getEmail())
            .build();
        return user;
    }

    private User userToDto(UserEntity user) {

        User dto = User.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .build();
        return dto;
    }
}
