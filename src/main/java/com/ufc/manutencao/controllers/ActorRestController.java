package com.ufc.manutencao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ufc.manutencao.domain.Actor;
import com.ufc.manutencao.domain.ActorView;
import com.ufc.manutencao.services.ActorService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

@RestController
@RequestMapping("/actors")
class ActorRestController {

    @Autowired
    private ActorService service;

    @RequestMapping(method = RequestMethod.GET)
    Collection<Actor> readActors() {
        return service.readAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{actorId}")
    Actor readActor(@PathVariable Long actorId) {
        return service.readOne(actorId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Actor> createActor(@RequestBody ActorView actorView) throws URISyntaxException {
        Actor newActor = service.save(actorView);
        return ResponseEntity.created(new URI("/actor/" + newActor.getId()))
                .header("Actor with id: " + newActor.getId() + " has been created", HttpStatus.CREATED.toString())
                .body(newActor);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{actorId}")
    ResponseEntity<Actor> updateActor(@RequestBody ActorView actorView, @PathVariable Long actorId) throws URISyntaxException {
        Actor updatedActor = service.update(actorView, actorId);
        return ResponseEntity.ok()
                .header("Actor with id: " + actorId + " has been updated", HttpStatus.ACCEPTED.toString())
                .body(updatedActor);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{actorId}")
    ResponseEntity<Actor> removeActor(@PathVariable Long actorId) {
        service.delete(actorId);
        return ResponseEntity.ok()
                .header("Actor with id: " + actorId + " has been deleted", HttpStatus.ACCEPTED.toString())
                .body(null);
    }
}
