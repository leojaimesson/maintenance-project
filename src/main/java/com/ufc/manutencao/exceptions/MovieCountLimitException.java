package com.ufc.manutencao.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieCountLimitException extends RuntimeException {

    public MovieCountLimitException(Long userId) {
        super(String.format("The user with id '%s' reached count limit.", Long.toString(userId)));
    }
}
