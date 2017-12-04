package com.ufc.manutencao.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
public class MovieView {

    @NotNull
    public String title;

    public String uri;
    @Id
    private Long id;
    @ElementCollection
    private Set<Actor> actors;

    public MovieView() {

    }

    public MovieView(Long id, String title) {
        this.id = id;
        this.actors = actors;
        this.title = title;
        this.uri = "/movies/" + id;
    }

    public MovieView(Long id, Set<Actor> actors, String title) {
        this.id = id;
        this.actors = actors;
        this.title = title;
        this.uri = "/movies/" + id;
    }

    public Long getId() {
        return id;
    }

    public Set<Actor> getActors() {
        return actors;
    }
}
