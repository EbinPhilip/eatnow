package com.eatnow.user.repositories.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eatnow.user.entities.UserAddress;
import com.eatnow.user.repositories.UserAddressRepository;

@Repository
public class UserAddressRepositoryDummy implements UserAddressRepository {

    private Map<Long, UserAddress> addresses;

    public UserAddressRepositoryDummy()
    {
        addresses = new HashMap<>();
        
        UserAddress a1 = UserAddress.builder()
            .id(1)
            .userId("u1")
            .index(1)
            .address("address")
            .latitude(22.3)
            .longitude(33.4)
            .build();
        UserAddress a2 = UserAddress.builder()
            .id(2)
            .userId("u1")
            .index(2)
            .address("address")
            .latitude(22.3)
            .longitude(33.4)
            .build();
        UserAddress a3 = UserAddress.builder()
            .id(3)
            .userId("u2")
            .index(1)
            .address("address")
            .latitude(22.3)
            .longitude(33.4)
            .build();
        UserAddress a4 = UserAddress.builder()
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
    public List<UserAddress> findByUserId(String userId)
    {
        List<UserAddress> addressList = new ArrayList<>();
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
    public UserAddress findByUserIdAndIndex(String userId, int index)
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
    public UserAddress create(UserAddress address)
    {
        address.setId(addresses.keySet().stream().count() + 1);
        return save(address);
    }

    @Override
    public UserAddress update(UserAddress address)
    {
        return save(address);
    }

    @Override
    public boolean delete(String userId, int index)
    {
        UserAddress old = addresses.values().stream().filter(
            (i) -> (
                i.getUserId().equals(userId) &&
                i.getIndex().equals(index)
            )
        ).findFirst()
        .orElseThrow(RuntimeException::new);
        addresses.remove(old.getId());
        return true;
    }

    private UserAddress save(UserAddress address)
    {
        addresses.put(address.getId(), address);
        return address;
    }
}
