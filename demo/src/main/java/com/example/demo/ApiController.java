package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class ApiController {

	@PostMapping("/user_json")
	public UserBean postUserJson(@RequestBody UserBean user) {
		return user;
	}

	@PostMapping("/user_form")
	public UserBean postUserForm(UserBean user) {
		return user;
	}
}
