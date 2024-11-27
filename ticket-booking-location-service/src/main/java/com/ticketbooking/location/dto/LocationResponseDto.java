package com.ticketbooking.location.dto;

import java.util.List;

import com.ticketbooking.location.entity.Location;

public class LocationResponseDto extends CommonApiResponse {

	private List<Location> location;

	public List<Location> getLocation() {
		return location;
	}

	public void setLocation(List<Location> location) {
		this.location = location;
	}

}
