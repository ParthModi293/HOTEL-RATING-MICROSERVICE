package com.hotel.service.services.impl;

import java.nio.channels.NonReadableChannelException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.hotel.service.entities.Hotel;
import com.hotel.service.exception.ResourceNotFoundException;
import com.hotel.service.repositories.HotelRepository;
import com.hotel.service.services.HotelService;

@Service
public class HotelServiceImpl implements HotelService{
	
	@Autowired
	private HotelRepository hotelRepository;

	@Override
	public Hotel create(Hotel hotel) {
		
		String hotelId = UUID.randomUUID().toString();
		hotel.setId(hotelId);
		
		return hotelRepository.save(hotel); 
		
		
		
	}

	@Override
	public List<Hotel> getAll() {
		// TODO Auto-generated method stub
		
		
		return hotelRepository.findAll();
	}

	@Override
	public Hotel get(String id) {
		// TODO Auto-generated method stub
		
		
		return hotelRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("hotel with given id not found"));
	}

}
