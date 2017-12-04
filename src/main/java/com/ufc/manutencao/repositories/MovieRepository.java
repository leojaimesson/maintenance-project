package com.ufc.manutencao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufc.manutencao.domain.Movie;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findById(Long id);
}

