package com.ufc.manutencao.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ufc.manutencao.domain.Actor;
import com.ufc.manutencao.domain.ActorView;
import com.ufc.manutencao.domain.Movie;
import com.ufc.manutencao.exceptions.ActorNotFoundException;
import com.ufc.manutencao.repositories.ActorRepository;
import com.ufc.manutencao.repositories.MovieRepository;
import com.ufc.manutencao.services.ActorService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Actor> readAll() {
        return actorRepository.findAll();
    }

    @Override
    public Actor readOne(Long id) {
        validateActor(id);
        return actorRepository.findOne(id);
    }

    @Override
    public Actor save(ActorView actorView) {
        return actorRepository.save(new Actor(actorView.getName()));
    }

    @Override
    public Actor update(ActorView actorView, Long id) {
        if (actorView.getMovieIds() != null) {
            Set<Movie> movies = new HashSet<>(movieRepository.findAll(actorView.getMovieIds()));
            return actorRepository.save(new Actor(movies, actorView.getName(), id));
        } else {
            if (!actorRepository.findById(id).isPresent()) {
                save(actorView);
            }
            return actorRepository.save(new Actor(actorView.getName(), id));
        }
    }

    @Override
    public void delete(Long id) {
        validateActor(id);
        actorRepository.delete(id);
    }


    private void validateActor(Long actorId) {
        actorRepository.findById(actorId).orElseThrow(
                () -> new ActorNotFoundException(actorId));
    }
}
