package com.ebin.eatnow.repositories.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ebin.eatnow.entities.UserAddress;
import com.ebin.eatnow.repositories.UserAddressRepository;
import com.ebin.eatnow.repositories.jpaDao.UserAddressJpaDao;

@Repository
@Primary
public class UserAddressRepositoryJpa implements UserAddressRepository {

    @Autowired
    UserAddressJpaDao dao;

    public List<UserAddress> findByUserId(String userId) {

        return dao.findByUserId(userId);
    }

    public UserAddress findByUserIdAndIndex(String userId, int index) {
        
        return dao.findByUserIdAndIndex(userId, index)
                .orElseThrow(RuntimeException::new);
    }

    public boolean existsByUserIdAndIndex(String userId, int index) {

        return dao.existsByUserIdAndIndex(userId, index);
    }

    @Transactional
    public UserAddress create(UserAddress address) {

        int index = (int)dao.findByUserId(address.getUserId()).stream().count() + 1;
        address.setIndex(index);

        return dao.save(address);
    }

    @Transactional
    public UserAddress update(UserAddress address) {

        UserAddress old = dao.findByUserIdAndIndex(address.getUserId(), address.getIndex())
                .orElseThrow(RuntimeException::new);
        address.setId(old.getId());

        return dao.save(address);
    }

    @Transactional
    public boolean delete(String userId, int index) {

        return (dao.deleteByUserIdAndIndex(userId, index) == 1);
    }
}
