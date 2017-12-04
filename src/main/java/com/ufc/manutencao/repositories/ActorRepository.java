package com.ufc.manutencao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufc.manutencao.domain.Actor;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findById(Long id);
}
