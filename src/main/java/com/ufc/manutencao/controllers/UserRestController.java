package com.ufc.manutencao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ufc.manutencao.domain.User;
import com.ufc.manutencao.services.UserService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<User> createUser(@RequestBody User userArg) throws URISyntaxException {
        User newUser = userService.save(userArg);
        return ResponseEntity.created(new URI("/users/" + newUser.getId()))
                .header("User has been created", HttpStatus.CREATED.toString())
                .body(newUser);
    }

}
