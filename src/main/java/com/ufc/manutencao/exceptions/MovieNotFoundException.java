package com.ufc.manutencao.exceptions;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(Long userId) {
        super(String.format("Could not find movie '%s'.", Long.toString(userId)));
    }
}
