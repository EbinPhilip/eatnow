package com.eatnow.user.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.user.dtos.UserAddressDto;
import com.eatnow.user.entities.UserAddress;
import com.eatnow.user.repositories.UserAddressRepository;

@Service
public class UserAddressService {
    @Autowired
    private UserAddressRepository addressRepository;
    
    public List<UserAddressDto> getAddressesByUserId(String userId) {

        return addressRepository.findByUserId(userId).stream().map(
            (i)->{
                return userAddressToDto(i);
            }).collect(Collectors.toList());
    }

    public UserAddressDto getAddressByUserIdAndIndex(String userId, int index) {

        UserAddress address = addressRepository.findByUserIdAndIndex(userId, index);
        return userAddressToDto(address);
    }

    @Transactional
    public UserAddressDto createAddress(String userId, UserAddressDto dto) {

        int index = (int)addressRepository.findByUserId(userId).stream().count() + 1;
        dto.setIndex(index);
        dto.setUserId(userId);

        UserAddress address = dtoToUserAddress(dto);
        addressRepository.create(address);

        return userAddressToDto(address);
    }

    @Transactional
    public UserAddressDto updateAddress(String userId, int index, UserAddressDto dto) {

        dto.setUserId(userId);
        dto.setIndex(index);      
  
        UserAddress address = dtoToUserAddress(dto);
        UserAddress old = addressRepository.findByUserIdAndIndex(userId, index);
        address.setId(old.getId());
        address = addressRepository.update(address);

        return userAddressToDto(address);
    }

    @Transactional
    public boolean deleteAddress(String userId, int index) {

        return addressRepository.delete(userId, index);
    }

    private UserAddress dtoToUserAddress(UserAddressDto dto) {

        UserAddress address = UserAddress.builder()
            .userId(dto.getUserId())
            .index(dto.getIndex())
            .address(dto.getAddress())
            .latitude(dto.getLatitude())
            .longitude(dto.getLongitude())
            .build();
        return address;
    }

    private UserAddressDto userAddressToDto(UserAddress address) {

        UserAddressDto dto = UserAddressDto.builder()
            .userId(address.getUserId())
            .index(address.getIndex())
            .address(address.getAddress())
            .latitude(address.getLatitude())
            .longitude(address.getLongitude())
            .build();
        return dto;
    }
}
