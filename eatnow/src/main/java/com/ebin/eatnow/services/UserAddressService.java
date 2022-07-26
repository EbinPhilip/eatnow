package com.ebin.eatnow.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebin.eatnow.dtos.UserAddressDto;
import com.ebin.eatnow.entities.UserAddress;
import com.ebin.eatnow.repositories.UserAddressRepository;
import com.ebin.eatnow.utils.Location;

@Service
public class UserAddressService {
    @Autowired
    private UserAddressRepository addressRepository;

    @Autowired
    private UserService userService;
    
    public List<UserAddressDto> getAddressesByUserId(String userId)
    {
        userService.getUserById(userId);
        return addressRepository.findByUserId(userId).stream().map(
            (i)->{
                return userAddressToDto(i);
            }).collect(Collectors.toList());
    }

    public UserAddressDto getAddressByUserIdAndIndex(String userId, int index)
    {
        UserAddress address = addressRepository.findByUserIdAndIndex(userId, index);
        return userAddressToDto(address);
    }

    public UserAddressDto createAddress(UserAddressDto dto)
    {
        userService.getUserById(dto.getUserId());
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
}
