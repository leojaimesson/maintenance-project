package com.ufc.manutencao.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
public class ActorView {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private List<Long> movieIds;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getMovieIds() {
        return movieIds;
    }
}
