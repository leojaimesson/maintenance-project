package com.ufc.manutencao.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ufc.manutencao.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findById(Long id);
}
