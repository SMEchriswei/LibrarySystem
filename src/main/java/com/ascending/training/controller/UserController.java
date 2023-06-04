package com.ascending.training.controller;

import com.ascending.training.model.User;
import com.ascending.training.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/users"})
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/{bookTitle}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @GetMapping(value = "/{email}", produces = "application/json")
    public User getUserBy(@PathVariable String email) {
        User user = userService.getUserByEmail(email);

        if (user != null) {
            logger.info(user.toString());
        }

        return user;
    }

}
