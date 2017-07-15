package io.github.mezk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.mezk.model.User;
import io.github.mezk.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user.getById", method = RequestMethod.GET)
    public @ResponseBody User getUserById(@RequestParam("user_id") Integer userId) {
       return userService.getById(userId);
    }

}
