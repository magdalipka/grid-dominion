package com.example.griddominion.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class Users {
    private static final List<String> users = List.of(
           "test 1",
           "test 2"
    );

    @GetMapping
    public List<String> getUsers(){
        return this.users;
    }
}
