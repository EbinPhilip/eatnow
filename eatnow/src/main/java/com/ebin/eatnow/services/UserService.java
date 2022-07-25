package com.ebin.eatnow.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebin.eatnow.dtos.UserAddressDto;
import com.ebin.eatnow.dtos.UserDto;
import com.ebin.eatnow.entities.User;
import com.ebin.eatnow.entities.UserAddress;
import com.ebin.eatnow.repositories.UserAddressRepository;
import com.ebin.eatnow.repositories.UserRepository;
import com.ebin.eatnow.utils.Location;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserAddressRepository addressRepository;

    public UserDto getUserById(String userId)
    {
        User user = repository.findById(userId);
        return userToDto(user);
    }

    public UserDto createUser(UserDto dto)
    {
        User user = dtoToUser(dto);
        repository.create(user);
        return userToDto(user);
    }

    public UserDto updateUser(UserDto dto)
    {
        User user = dtoToUser(dto);
        repository.update(user);
        return userToDto(user);
    }

    public List<UserAddressDto> getAddressByUserId(String userId)
    {
        repository.findById(userId);
        return addressRepository.findByUserId(userId).stream().map(
            (i)->{
                return userAddressToDto(i);
            }).collect(Collectors.toList());
    }

    public UserAddressDto createAddress(UserAddressDto dto)
    {
        repository.findById(dto.getUserId());
        UserAddress address = dtoToUserAddress(dto);
        addressRepository.create(address);
        return userAddressToDto(address);
    }

    public UserAddressDto updateAddress(UserAddressDto dto)
    {        
        UserAddress address = dtoToUserAddress(dto);
        address = addressRepository.update(address);
        return userAddressToDto(address);
    }

    public boolean deleteAddress(String userId, int index)
    {
        return addressRepository.delete(userId, index);
    }

    private UserAddress dtoToUserAddress(UserAddressDto dto)
    {
        UserAddress address = UserAddress.builder()
            .userId(dto.getUserId())
            .index(dto.getIndex())
            .address(dto.getAddress())
            .location(new Location(dto.getLatitude(), dto.getLongitude()))
            .build();
        return address;
    }

    private UserAddressDto userAddressToDto(UserAddress address)
    {
        UserAddressDto dto = UserAddressDto.builder()
            .userId(address.getUserId())
            .index(address.getIndex())
            .address(address.getAddress())
            .latitude(address.getLocation().getLatitude())
            .longitude(address.getLocation().getLongitude())
            .build();
        return dto;
    }

    private User dtoToUser(UserDto dto)
    {
        User user = User.builder()
            .id(dto.getId())
            .name(dto.getName())
            .phone(dto.getPhone())
            .email(dto.getEmail())
            .build();
        return user;
    }

    private UserDto userToDto(User user)
    {
        UserDto dto = UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .build();
        return dto;
    }

    
}
