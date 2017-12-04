package com.ufc.manutencao.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
public class BorrowView {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Long userId;

    private Double cost;

    @ElementCollection
    private Collection<MovieView> movies;

    public BorrowView() {

    }

    public BorrowView(Long userId, Double cost, Collection<MovieView> movies) {
        this.userId = userId;
        this.cost = cost;
        this.movies = movies;
    }

    public Long getUserId() {
        return userId;
    }

    public Collection<MovieView> getMovies() {
        return movies;
    }

    public void setMovies(Collection<MovieView> movies) {
        this.movies = movies;
    }

    public Double getCost() {
        return cost;
    }
}
