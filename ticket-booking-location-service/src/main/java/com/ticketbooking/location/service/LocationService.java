package com.ticketbooking.location.service;

import java.util.List;

import com.ticketbooking.location.entity.Location;

public interface LocationService {
	
	Location addLocation(Location location);
	
	Location getLocationById(int location);
	
	List<Location> getAllActiveLocation(int status);

	Location udpateLocation(Location location);
	
	List<Location> searchLocationsByName(String locationName);
}
