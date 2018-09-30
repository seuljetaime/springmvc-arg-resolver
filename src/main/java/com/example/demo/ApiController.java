package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class ApiController {

    @PostMapping("/user")
    public UserBean postUser(@RequestBody UserBean userBean) {
        return userBean;
    }
}
