package com.ufc.manutencao.services;

import java.util.List;

import com.ufc.manutencao.domain.BorrowView;
import com.ufc.manutencao.domain.Movie;

public interface MovieService {
    List<Movie> readAll();

    Movie readOne(Long id);

    Movie save(Movie movie);

    Movie update(Movie movie, Long id);

    void delete(Long id);

    BorrowView borrow(BorrowView borrowView);

    List<Movie> returnMovies (BorrowView borrowView);

    List<Movie> readAvailable();

    List<Movie> readByCategory(String category);

    List<Movie> readByUser(Long id);
}
