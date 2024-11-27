package com.ticketbooking.book.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.lowagie.text.DocumentException;
import com.ticketbooking.book.dto.AddScheduleTrainTicketRequestDto;
import com.ticketbooking.book.dto.BookTrainRequestDto;
import com.ticketbooking.book.dto.BookingResponseDto;
import com.ticketbooking.book.dto.BookingTicketCountResponse;
import com.ticketbooking.book.dto.CommonApiResponse;
import com.ticketbooking.book.resource.TrainBookingResource;

@RestController
@RequestMapping("/api/book/")
@CrossOrigin(origins = "http://localhost:3000")
public class TrainBookingController {
	
	@Autowired
	private TrainBookingResource trainBookingResource;
	
	@PostMapping("/train")
	public ResponseEntity<CommonApiResponse> bookTrain(@RequestBody BookTrainRequestDto bookingRequest) {
		return trainBookingResource.bookTrain(bookingRequest);
	}
	
	@PostMapping("/add/scheduled/train/tickets/")
	public ResponseEntity<CommonApiResponse> addScheduledTrainTickets(@RequestBody AddScheduleTrainTicketRequestDto ticketAddRequest) {
		return trainBookingResource.addScheduledTrainTickets(ticketAddRequest);
	}
	
	@DeleteMapping("/ticket/cancel")
	public ResponseEntity<CommonApiResponse> cancelTrainTicket(@RequestParam("bookingId") int bookingId) {
		return trainBookingResource.cancelTrainBooking(bookingId);
	}
	
	@GetMapping("/fetch/all")
	public ResponseEntity<BookingResponseDto> fetchAllBooking() {
		return trainBookingResource.fetchAllTrainBooking();
	}
	
	@GetMapping("/fetch/scheduled/train/tickets")
	public ResponseEntity<BookingResponseDto> fetchScheduledTrainBookings(@RequestParam("scheduleTrainId") int scheduleTrainId) {
		return trainBookingResource.fetchScheduledTrainBookings(scheduleTrainId);
	}
	
	@GetMapping("/fetch/user")
	public ResponseEntity<BookingResponseDto> fetchUserBookings(@RequestParam("userId") int userId) {
		return trainBookingResource.fetchBookingByUserId(userId);
	}
	
	@GetMapping("/fetch/schedule/train")
	public ResponseEntity<BookingResponseDto> fetchScheduledTrainAvailableTickets(@RequestParam("scheduleTrainId") int scheduleTrainId) {
		return trainBookingResource.fetchBookingByScheduleTrainId(scheduleTrainId);
	}
	
	@GetMapping("/fetch/schedule/train/ticket/count")
	public ResponseEntity<BookingTicketCountResponse> fetchScheduledTrainTicketCounts(@RequestParam("scheduleTrainId") int scheduleTrainId) {
		return trainBookingResource.fetchScheduledTrainTicketCounts(scheduleTrainId);
	}
	
	@GetMapping("/download/ticket")
	public void downloadBill(@RequestParam("bookingId") String bookingId, HttpServletResponse response) throws Exception {
		
		trainBookingResource.downloadBookingTicket(bookingId, response);

	}

}
