package com.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.aspectj.weaver.ArrayAnnotationValue;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.service.entities.Hotel;
import com.user.service.entities.Rating;
import com.user.service.entities.User;
import com.user.service.exception.ResourceNotFoundException;
import com.user.service.external.services.HotelService;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserService;

import ch.qos.logback.classic.Logger;

@Service
public class UserServiceImple implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private RestTemplate restTemplate;
	
	@Autowired
	private HotelService hotelService;
	
	
	private org.slf4j.Logger logger = LoggerFactory.getLogger(UserServiceImple.class);
	
	@Override
	public User saveUser(User user) {
		
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		
		return userRepository.findAll();
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		 User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found with this ID : " + userId));
		
		 // fetch rating from the above user
		 //http://localhost:8080/ratings/hotels/616c80bf-1b97-41ee-939d-45dc5743936e
		 
		 Rating[]  ratingsFromUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
		 logger.info("{} ",ratingsFromUser);
		 
		 List<Rating> ratings = Arrays.stream(ratingsFromUser).toList();
		
		 List<Rating> ratingList = ratings.stream().map(rating ->{
			 
			// ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
			 
			 Hotel hotel = hotelService.getHotel(rating.getHotelId());
			// logger.info("response status code: {} ", forEntity.getStatusCode());
			 rating.setHotel(hotel);
			 return rating;
		 }).collect(Collectors.toList());
		
			 // api call to hotel service to get hotel
			 // set the hotel to rating
			 //return the rating
			 
			 
			 
		
		 
		 user.setRatings(ratingList);
		 
		 return user;
	}

}
