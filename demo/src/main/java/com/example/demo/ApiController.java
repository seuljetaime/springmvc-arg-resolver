package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

	@GetMapping("/user_query")
	public UserBean getUser(@RequestParam String username, @RequestParam Date birthday) {
		UserBean user = new UserBean();
		user.setUsername(username);
		user.setBirthday(birthday);
		return user;
	}
}
