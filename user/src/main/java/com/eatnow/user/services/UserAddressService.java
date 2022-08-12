package com.eatnow.user.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.user.dtos.UserAddress;
import com.eatnow.user.entities.UserAddressEntity;
import com.eatnow.user.repositories.UserAddressRepository;

@Service
public class UserAddressService {
    @Autowired
    private UserAddressRepository addressRepository;
    
    public List<UserAddress> getAddressesByUserId(String userId) {

        return addressRepository.findByUserId(userId).stream().map(
            (i)->{
                return userAddressToDto(i);
            }).collect(Collectors.toList());
    }

    public UserAddress getAddressByUserIdAndIndex(String userId, int index) {

        UserAddressEntity address = addressRepository.findByUserIdAndIndex(userId, index);
        return userAddressToDto(address);
    }

    @Transactional
    public UserAddress createAddress(String userId, UserAddress dto) {

        int index = (int)addressRepository.findByUserId(userId).stream().count() + 1;
        dto.setIndex(index);
        dto.setUserId(userId);

        UserAddressEntity address = dtoToUserAddress(dto);
        addressRepository.create(address);

        return userAddressToDto(address);
    }

    @Transactional
    public UserAddress updateAddress(String userId, int index, UserAddress dto) {

        dto.setUserId(userId);
        dto.setIndex(index);      
  
        UserAddressEntity address = dtoToUserAddress(dto);
        UserAddressEntity old = addressRepository.findByUserIdAndIndex(userId, index);
        address.setId(old.getId());
        address = addressRepository.update(address);

        return userAddressToDto(address);
    }

    @Transactional
    public boolean deleteAddress(String userId, int index) {

        return addressRepository.delete(userId, index);
    }

    private UserAddressEntity dtoToUserAddress(UserAddress dto) {

        UserAddressEntity address = UserAddressEntity.builder()
            .userId(dto.getUserId())
            .index(dto.getIndex())
            .address(dto.getAddress())
            .latitude(dto.getLatitude())
            .longitude(dto.getLongitude())
            .build();
        return address;
    }

    private UserAddress userAddressToDto(UserAddressEntity address) {

        UserAddress dto = UserAddress.builder()
            .userId(address.getUserId())
            .index(address.getIndex())
            .address(address.getAddress())
            .latitude(address.getLatitude())
            .longitude(address.getLongitude())
            .build();
        return dto;
    }
}
