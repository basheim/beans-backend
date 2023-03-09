package com.beandon.backend.controllers;

import com.beandon.backend.pojo.users.Auth;
import com.beandon.backend.pojo.users.PartialUser;
import com.beandon.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public void createUser(@RequestBody PartialUser user) {
        userService.createUser(user);
    }

    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

    @PostMapping("/authenticate")
    public void authenticate(@RequestBody Auth auth) {

    }
}
