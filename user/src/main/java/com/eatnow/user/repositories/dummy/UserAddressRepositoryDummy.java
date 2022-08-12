package com.eatnow.user.repositories.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eatnow.user.entities.UserAddressEntity;
import com.eatnow.user.repositories.UserAddressRepository;

@Repository
public class UserAddressRepositoryDummy implements UserAddressRepository {

    private Map<Long, UserAddressEntity> addresses;

    public UserAddressRepositoryDummy()
    {
        addresses = new HashMap<>();
        
        UserAddressEntity a1 = UserAddressEntity.builder()
            .id(1)
            .userId("u1")
            .index(1)
            .address("address")
            .latitude(22.3)
            .longitude(33.4)
            .build();
        UserAddressEntity a2 = UserAddressEntity.builder()
            .id(2)
            .userId("u1")
            .index(2)
            .address("address")
            .latitude(22.3)
            .longitude(33.4)
            .build();
        UserAddressEntity a3 = UserAddressEntity.builder()
            .id(3)
            .userId("u2")
            .index(1)
            .address("address")
            .latitude(22.3)
            .longitude(33.4)
            .build();
        UserAddressEntity a4 = UserAddressEntity.builder()
            .id(4)
            .userId("u2")
            .index(2)
            .address("address")
            .latitude(22.3)
            .longitude(33.4)
            .build();

        save(a1);
        save(a2);
        save(a3);
        save(a4);
    }

    @Override
    public List<UserAddressEntity> findByUserId(String userId)
    {
        List<UserAddressEntity> addressList = new ArrayList<>();
        addressList.addAll(
            addresses.values().stream().filter(
            (i)->(
                i.getUserId().equals(userId)
            ))
            .collect(Collectors.toList())
        );

        return addressList;
    }

    @Override
    public UserAddressEntity findByUserIdAndIndex(String userId, int index)
    {
        return addresses.values().stream().filter(
            (i)->(
                i.getUserId().equals(userId)
                && i.getIndex().equals(index)
            ))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    @Override
    public boolean existsByUserIdAndIndex(String userId, int index)
    {
        return addresses.values().stream().filter(
            (i)->(
                i.getUserId().equals(userId)
                && i.getIndex().equals(index)
            ))
            .count() == 1;
    }

    @Override
    public UserAddressEntity create(UserAddressEntity address)
    {
        address.setId(addresses.keySet().stream().count() + 1);
        return save(address);
    }

    @Override
    public UserAddressEntity update(UserAddressEntity address)
    {
        return save(address);
    }

    @Override
    public boolean delete(String userId, int index)
    {
        UserAddressEntity old = addresses.values().stream().filter(
            (i) -> (
                i.getUserId().equals(userId) &&
                i.getIndex().equals(index)
            )
        ).findFirst()
        .orElseThrow(RuntimeException::new);
        addresses.remove(old.getId());
        return true;
    }

    private UserAddressEntity save(UserAddressEntity address)
    {
        addresses.put(address.getId(), address);
        return address;
    }
}
