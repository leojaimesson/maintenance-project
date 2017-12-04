package com.ufc.manutencao.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActorNotFoundException extends RuntimeException {

    public ActorNotFoundException(Long actorId) {
        super(String.format("Could not find actor '%s'.", Long.toString(actorId)));
    }
}
