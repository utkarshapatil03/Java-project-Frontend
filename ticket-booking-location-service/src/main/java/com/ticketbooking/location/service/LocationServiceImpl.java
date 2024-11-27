package com.ticketbooking.location.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketbooking.location.dao.LocationDao;
import com.ticketbooking.location.entity.Location;

@Service
public class LocationServiceImpl implements LocationService {
	
	@Autowired
	private LocationDao departmentDao;

	@Override
	public Location addLocation(Location location) {
		return departmentDao.save(location);
	}

	@Override
	public Location getLocationById(int departmentId) {
		return departmentDao.findById(departmentId).get();
	}

	@Override
	public List<Location> getAllActiveLocation(int status) {
		return departmentDao.findByStatus(status);
	}

	@Override
	public Location udpateLocation(Location location) {
		return departmentDao.save(location);
	}

	@Override
	public List<Location> searchLocationsByName(String locationName) {
		return departmentDao.findByNameContainingIgnoreCase(locationName);
	}

}
