package com.ticketbooking.book.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ticketbooking.book.dto.ScheduleTrainResponseDto;
import com.ticketbooking.book.dto.TrainDetailResponseDto;

@Component
@FeignClient(name = "ticket-booking-train-service")
public interface TrainService {

	@GetMapping("/api/train/schedule/fetch/id")
	ScheduleTrainResponseDto fetchScheduledTrainById(@RequestParam("scheduleTrainId") int  scheduleTrainId);
	
	@GetMapping("/fetch/location/search")
	public ResponseEntity<TrainDetailResponseDto> fetchTrainDetailsFromAndToLocation(
			@RequestParam("fromLocationId") int fromLocationId, @RequestParam("toLocationId") int toLocationId);
	
}
