package com.ebin.eatnow.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.ebin.eatnow.dtos.UserDto;
import com.ebin.eatnow.entities.User;
import com.ebin.eatnow.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public UserDto getUserById(String userId) {

        User user = repository.findById(userId);
        return userToDto(user);
    }

    @Transactional
    public UserDto createUser(UserDto dto) {

        User user = dtoToUser(dto);
        repository.create(user);
        return userToDto(user);
    }

    @Transactional
    public UserDto updateUser(String userId, UserDto dto) {

        dto.setId(userId);
        User user = dtoToUser(dto);
        repository.update(user);
        return userToDto(user);
    }

    private User dtoToUser(UserDto dto) {

        User user = User.builder()
            .id(dto.getId())
            .name(dto.getName())
            .phone(dto.getPhone())
            .email(dto.getEmail())
            .build();
        return user;
    }

    private UserDto userToDto(User user) {

        UserDto dto = UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .build();
        return dto;
    }
}
