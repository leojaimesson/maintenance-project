package com.ufc.manutencao.services;

import java.util.List;

import com.ufc.manutencao.domain.Actor;
import com.ufc.manutencao.domain.ActorView;

public interface ActorService {
    List<Actor> readAll();

    Actor readOne(Long id);

    Actor save(ActorView actorView);

    Actor update(ActorView actorView, Long id);

    void delete(Long id);
}
