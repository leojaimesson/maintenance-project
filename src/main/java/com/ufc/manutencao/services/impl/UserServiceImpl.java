package com.ufc.manutencao.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufc.manutencao.domain.User;
import com.ufc.manutencao.repositories.UserRepository;
import com.ufc.manutencao.services.UserService;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User userArg) {
        return userRepository.save(new User(userArg.getName()));
    }
}
