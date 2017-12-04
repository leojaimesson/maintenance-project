package com.ufc.manutencao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Movie {
    @NotNull
    public String title;

    @ManyToMany(mappedBy = "movies")
    private Set<Actor> actors = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    private User borrower;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String category;

    public Movie(String title) {
        this.title = title;
    }

    public Movie(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public Movie(String title, String category, Long id) {
        this.title = title;
        this.category = category;
        this.id = id;
    }

    public Movie(String title, Long id) {
        this.title = title;
        this.id = id;
    }

    public Movie() {

    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getBorrower() {
        return borrower;
    }

    public Movie setBorrower(User borrower) {
        this.borrower = borrower;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        if (borrower == null) {
            return true;
        } else {
            return false;
        }
    }
}
