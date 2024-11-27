package com.ticketbooking.location.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbooking.location.constants.DatabaseConstants.LocationStatus;
import com.ticketbooking.location.dto.CommonApiResponse;
import com.ticketbooking.location.dto.LocationResponseDto;
import com.ticketbooking.location.entity.Location;
import com.ticketbooking.location.service.LocationService;

@Component
public class LocationResource {

	@Autowired
	private LocationService locationService;

	ObjectMapper objectMapper = new ObjectMapper();

	public ResponseEntity<CommonApiResponse> addLocation(Location location) {

		CommonApiResponse response = new CommonApiResponse();

		if (location == null) {
			response.setResponseMessage("location is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		location.setStatus(LocationStatus.ACTIVE.value());
		
		Location addedLocation = this.locationService.addLocation(location);

		if (addedLocation == null) {
			response.setResponseMessage("Failed to add Location");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Location added Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<LocationResponseDto> getLocationById(int locationId) {

		LocationResponseDto response = new LocationResponseDto();

		if (locationId == 0) {
			response.setResponseMessage("location id is null");
			response.setSuccess(true);

			return new ResponseEntity<LocationResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Location addedLocation = this.locationService.getLocationById(locationId);

		if (addedLocation == null) {
			response.setResponseMessage("Failed to get the Location");
			response.setSuccess(true);

			return new ResponseEntity<LocationResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		response.setLocation(Arrays.asList(addedLocation));
		response.setResponseMessage("Location added Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<LocationResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<LocationResponseDto> getAllLocation() {

		LocationResponseDto response = new LocationResponseDto();

		List<Location> locations = new ArrayList<>();
		locations = this.locationService.getAllActiveLocation(LocationStatus.ACTIVE.value());

		response.setLocation(locations);
		response.setResponseMessage("Locations fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<LocationResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> deleteLocationById(int locationId) {

		CommonApiResponse response = new CommonApiResponse();

		if (locationId == 0) {
			response.setResponseMessage("location id is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Location location = this.locationService.getLocationById(locationId);

		if (location == null) {
			response.setResponseMessage("Location not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		location.setStatus(LocationStatus.NOT_ACTIVE.value());

		Location updatedLocation = this.locationService.udpateLocation(location);

		if (updatedLocation == null) {
			response.setResponseMessage("Failed to delete the Location");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Location Deleted Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> updateLocation(Location location) {

		CommonApiResponse response = new CommonApiResponse();

		if (location == null) {
			response.setResponseMessage("location is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		Location existingLocation = this.locationService.getLocationById(location.getId());
		
		if (existingLocation == null) {
			response.setResponseMessage("location not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(location.getName() != null) {
			existingLocation.setName(location.getName());
		}
		
		if(location.getDescription() != null) {
			existingLocation.setDescription(location.getDescription());
		}
		
		Location updatedLocation = this.locationService.udpateLocation(existingLocation);

		if (updatedLocation == null) {
			response.setResponseMessage("Failed to update Location");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Location updated Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}
	
	public ResponseEntity<LocationResponseDto> searchLocationsByName(String locationName) {

		LocationResponseDto response = new LocationResponseDto();

		List<Location> locations = new ArrayList<>();
		
		if (locationName == null || locationName.equals("")) {
			response.setResponseMessage("Please enter the valid text to search the locations");
			response.setSuccess(true);

			return new ResponseEntity<LocationResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		locations = this.locationService.searchLocationsByName(locationName);

		response.setLocation(locations);
		response.setResponseMessage("Locations fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<LocationResponseDto>(response, HttpStatus.OK);

	}

}
