package com.user.service.services;

import java.sql.Struct;
import java.util.List;

import com.user.service.entities.User;

public interface UserService {

	// user operations
	
	//create user
	
	User saveUser(User user);
	
	//get all user
	
	List<User> getAllUser();
	
	//get user by UserID
	
	User getUser(String userId);
}
