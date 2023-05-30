package com.user.service.controller;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.entities.User;
import com.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	private org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
	
	// create user
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user){
		
		User user1 = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
	}
	
	
	// get single user
	
	@GetMapping("/{userId}")
	@CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallBack")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId){
		User user = userService.getUser(userId);
		return ResponseEntity.ok(user);
		
	}
	
	public ResponseEntity<User> ratingHotelFallBack(String userId, Exception ex){
		
		ex.printStackTrace();
		
		logger.info("Fallback is executed because service is down: ", ex.getMessage());
		User user =   User.builder().email("dummay@gmail.com").about("this user is created dummy bcz service down").name("Dummy").userId("12345").build();

		return new ResponseEntity<>(user, HttpStatus.OK);
	
	}
	
	
	
	
	//get all user..
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUser(){
		
		List<User> allUser = userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}
	
	
	
	
	
	

}
