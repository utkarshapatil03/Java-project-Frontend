package com.ticketbooking.location.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketbooking.location.entity.Location;

@Repository
public interface LocationDao extends JpaRepository<Location, Integer> {
	
	List<Location> findByStatus(int status);
	List<Location> findByNameContainingIgnoreCase(String locationName);

}
