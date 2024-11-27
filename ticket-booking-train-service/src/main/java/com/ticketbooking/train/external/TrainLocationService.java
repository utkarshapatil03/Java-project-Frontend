package com.ticketbooking.train.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ticketbooking.train.dto.LocationResponseDto;

@Component
@FeignClient(name = "ticket-booking-location-service")
public interface TrainLocationService {
	
	@GetMapping("api/location/fetch")
	LocationResponseDto fetchLocationById(@RequestParam("locationId") int  locationId);
	
}
