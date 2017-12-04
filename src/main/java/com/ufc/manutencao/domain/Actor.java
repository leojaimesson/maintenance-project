package com.ufc.manutencao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
public class Actor {

    @JsonIgnore
    @ManyToMany
    private Set<Movie> movies;

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Actor() {

    }

    public Actor(String name) {
        this.name = name;
    }

    public Actor(String name, Long id) {
        this(name);
        this.id = id;
    }

    public Actor(Set<Movie> movie, String name) {
        this(name);
        this.movies = movie;
    }

    public Actor(Set<Movie> movie, String name, Long id) {
        this(movie, name);
        this.id = id;
    }

    public void unmapMovie(Long movieId) {
        for (Movie movie : movies) {
            if (movie.getId().equals(movieId)) {
                this.movies.remove(movie);
            }
        }
    }

}
