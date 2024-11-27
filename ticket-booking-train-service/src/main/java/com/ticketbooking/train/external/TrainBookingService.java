package com.ticketbooking.train.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ticketbooking.train.dto.AddScheduleTrainTicketRequestDto;
import com.ticketbooking.train.dto.CommonApiResponse;

@Component
@FeignClient(name = "ticket-booking-book-service")
public interface TrainBookingService {
	
	@PostMapping("/api/book/add/scheduled/train/tickets/")
	public ResponseEntity<CommonApiResponse> addScheduledTrainTickets(@RequestBody AddScheduleTrainTicketRequestDto ticketAddRequest);

}
