package org.soneech.springcourse.controller;

import org.soneech.springcourse.model.User;
import org.soneech.springcourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MainController {
    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> usersInfo() {
        return userService.findAll();
    }
}
