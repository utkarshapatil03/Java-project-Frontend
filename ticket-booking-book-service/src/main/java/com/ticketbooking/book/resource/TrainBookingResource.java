package com.ticketbooking.book.resource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.ticketbooking.book.constants.DatabaseConstant.BookingStatus;
import com.ticketbooking.book.dto.AddScheduleTrainTicketRequestDto;
import com.ticketbooking.book.dto.BookTrainRequestDto;
import com.ticketbooking.book.dto.BookingResponse;
import com.ticketbooking.book.dto.BookingResponseDto;
import com.ticketbooking.book.dto.BookingTicketCountResponse;
import com.ticketbooking.book.dto.CommonApiResponse;
import com.ticketbooking.book.dto.ScheduleTrainResponseDto;
import com.ticketbooking.book.dto.TrainDetail;
import com.ticketbooking.book.dto.TrainSeat;
import com.ticketbooking.book.dto.User;
import com.ticketbooking.book.dto.UserListResponseDto;
import com.ticketbooking.book.entity.TrainBooking;
import com.ticketbooking.book.external.TrainService;
import com.ticketbooking.book.external.UserService;
import com.ticketbooking.book.service.TrainBookingService;
import com.ticketbooking.book.utility.DateTimeUtil;
import com.ticketbooking.book.utility.TicketDownloader;
import com.ticketbooking.book.utility.TrainBookingIdGenerator;

@Component
public class TrainBookingResource {

	@Autowired
	private TrainBookingService trainBookingService;

	@Autowired
	private TrainService trainService;

	@Autowired
	private UserService userService;

	private ObjectMapper objectMapper = new ObjectMapper();

	public ResponseEntity<CommonApiResponse> bookTrain(BookTrainRequestDto bookingRequest) {

		CommonApiResponse response = new CommonApiResponse();

		if (bookingRequest == null) {
			response.setResponseMessage("Bad Request, improper request data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (bookingRequest.getScheduleTrainId() == 0) {
			response.setResponseMessage("Bad Request, schedule train id missing");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String bookingId = TrainBookingIdGenerator.generate();
		String bookingTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<TrainBooking> totalTrainBookings = this.trainBookingService
				.getByScheduleTrainId(bookingRequest.getScheduleTrainId());

		List<TrainBooking> totalAvailableBookings = new ArrayList<>();
		List<TrainBooking> totalWaitingBookings = new ArrayList<>();

		for (TrainBooking totalTrainBooking : totalTrainBookings) {

			if (totalTrainBooking.getStatus() == BookingStatus.AVAILABLE.value()) {
				totalAvailableBookings.add(totalTrainBooking);
				continue;
			}

			if (totalTrainBooking.getStatus() == BookingStatus.WAITING.value()) {
				totalWaitingBookings.add(totalTrainBooking);
				continue;
			}

		}

		int totalAvailableSeat = totalAvailableBookings.size();
		int totalWaitingSeat = totalWaitingBookings.size();

		// all the seats are available
		if (bookingRequest.getTotalSeat() <= totalAvailableSeat) {

			for (int i = 0; i < bookingRequest.getTotalSeat(); i++) {
				TrainBooking booking = totalAvailableBookings.get(i);
				booking.setBookingId(bookingId);
				booking.setBookingTime(bookingTime);
				booking.setStatus(BookingStatus.CONFIRMED.value());
				booking.setUserId(bookingRequest.getUserId());
				this.trainBookingService.updateBooking(booking);
			}

		}

		// all seats are not available
		else {

			// that means already some users booking is in waiting, so this bookings will
			// also be in waiting no doubt
			if (totalWaitingSeat > 0) {

				for (int i = 0; i < bookingRequest.getTotalSeat(); i++) {
					TrainBooking existingTrainBooking = totalTrainBookings.get(i); // for adding new entry in
																					// WAITING

					TrainBooking bookingInWaiting = TrainBooking.builder()
							.scheduleTrainId(existingTrainBooking.getScheduleTrainId())
							.trainId(existingTrainBooking.getTrainId()).userId(bookingRequest.getUserId())
							.bookingId(bookingId).bookingTime(bookingTime).status(BookingStatus.WAITING.value())
							.build();
					this.trainBookingService.bookTrain(bookingInWaiting);
				}

			}

			// now here we can clearly say that total seat which we require is NOT FULLY
			// AVAILABLE
			// some seat are available and some will have to be in WAITING
			else {

				int totalSeatsToAddInWaiting = bookingRequest.getTotalSeat() - totalAvailableSeat;

				// firstly confirm all the available seats
				for (TrainBooking trainBooking : totalAvailableBookings) {
					trainBooking.setBookingId(bookingId);
					trainBooking.setBookingTime(bookingTime);
					trainBooking.setStatus(BookingStatus.CONFIRMED.value());
					trainBooking.setUserId(bookingRequest.getUserId());
					this.trainBookingService.updateBooking(trainBooking);
				}

				// secondly add pending seats as waiting
				for (int i = 0; i < totalSeatsToAddInWaiting; i++) {
					TrainBooking existingTrainBooking = totalTrainBookings.get(i); // for adding new entry in WAITING

					TrainBooking bookingInWaiting = TrainBooking.builder()
							.scheduleTrainId(existingTrainBooking.getScheduleTrainId())
							.trainId(existingTrainBooking.getTrainId()).userId(bookingRequest.getUserId())
							.bookingId(bookingId).bookingTime(bookingTime).status(BookingStatus.WAITING.value())
							.build();
					this.trainBookingService.bookTrain(bookingInWaiting);
				}

			}

		}

		response.setResponseMessage(
				"Your Booking ID is [ " + bookingId + " ] , Please check the booking status in dashboard");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

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

	@Transactional
	public ResponseEntity<CommonApiResponse> addScheduledTrainTickets(
			AddScheduleTrainTicketRequestDto addTicketRequest) {

		CommonApiResponse response = new CommonApiResponse();

		if (addTicketRequest.getScheduleTrainId() == 0) {
			response.setResponseMessage("Bad Request, improper request data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (addTicketRequest.getTrainSeats() == null || addTicketRequest.getTrainSeats().isEmpty()) {
			response.setResponseMessage("Bad Request, schedule train seats not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		for (TrainSeat seat : addTicketRequest.getTrainSeats()) {

			// using builder design pattern for creating the required object
			TrainBooking booking = TrainBooking.builder().scheduleTrainId(addTicketRequest.getScheduleTrainId())
					.trainId(seat.getTrainId()).trainSeatId(seat.getId()).status(BookingStatus.AVAILABLE.value())
					.trainSeat(seat.getSeatNo()).build();
			this.trainBookingService.bookTrain(booking);

		}

		response.setResponseMessage("Scheduled Train Tickets Added in system");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

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

	@Transactional
	public ResponseEntity<CommonApiResponse> cancelTrainBooking(int bookingId) {

		CommonApiResponse response = new CommonApiResponse();

		if (bookingId == 0) {
			response.setResponseMessage("Failed to Cancel Booking, missing booking id");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		TrainBooking booking = this.trainBookingService.getTrainBookingById(bookingId);

		if (booking == null) {
			response.setResponseMessage("No Booking found, failed to cancel booking");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (booking.getStatus() == BookingStatus.CANCELLED.value()) {
			response.setResponseMessage("Ticket is already cancelled");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (booking.getStatus() == BookingStatus.WAITING.value()) {

			booking.setStatus(BookingStatus.CANCELLED.value());
			trainBookingService.updateBooking(booking);

			response.setResponseMessage("Ticket Cancelled Successfully");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}

		if (booking.getStatus() == BookingStatus.CONFIRMED.value()) {

			booking.setStatus(BookingStatus.CANCELLED.value());
			trainBookingService.updateBooking(booking);

			// now making new ticket available to customer with the same seat
			TrainBooking ticketToMakeAvailable = TrainBooking.builder().scheduleTrainId(booking.getScheduleTrainId())
					.trainId(booking.getTrainId()).status(BookingStatus.AVAILABLE.value())
					.trainSeatId(booking.getTrainId()).trainSeat(booking.getTrainSeat()).build();

			trainBookingService.updateBooking(ticketToMakeAvailable);

			response.setResponseMessage("Ticket Cancelled Successfully");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
		return null;

	}

	public ResponseEntity<BookingResponseDto> fetchAllTrainBooking() {

		BookingResponseDto response = new BookingResponseDto();

		List<TrainBooking> trainBookings = this.trainBookingService
				.getByStatusNotOrderByIdDesc(BookingStatus.AVAILABLE.value());

		if (trainBookings == null) {
			response.setResponseMessage("No Bookings made by users...!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		List<BookingResponse> bookings = new ArrayList<>();

		BookingResponse booking = null;

		for (TrainBooking trainBooking : trainBookings) {
			booking = new BookingResponse();

			booking.setBookId(trainBooking.getId());
			booking.setBookingId(trainBooking.getBookingId());
			booking.setBookingTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(trainBooking.getBookingTime()));
			booking.setUserId(trainBooking.getUserId());
			booking.setTrainId(trainBooking.getTrainId());
			booking.setTrainSeatId(trainBooking.getTrainSeatId());
			booking.setScheduleTrainId(trainBooking.getScheduleTrainId());

			// hitting external service (TRAIN SERVICE)
			ScheduleTrainResponseDto scheduleTrainDetail = trainService
					.fetchScheduledTrainById(booking.getScheduleTrainId());
			TrainDetail train = scheduleTrainDetail.getTrain().get(0);
			List<TrainSeat> trainSeats = train.getTrainSeats();

			booking.setTrainName(train.getName());
			booking.setTrainNumber(train.getNumber());
			booking.setFromLocation(train.getFromLocation());
			booking.setToLocation(train.getToLocation());
			booking.setSeatPrice(train.getSeatPrice());
			booking.setScheduleTrainTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(train.getScheduleTime()));
			booking.setStatus(BookingStatus.getStatusString(trainBooking.getStatus()));

			for (TrainSeat trainSeat : trainSeats) {
				if (trainSeat.getId() == booking.getTrainSeatId()) {
					booking.setTrainSeat(trainSeat.getSeatNo());
					break;
				}
			}

			// calling user service to fetch User by using user id
			UserListResponseDto userResponse = userService.fetchUserById(trainBooking.getUserId());
			List<User> users = userResponse.getUsers();

			booking.setUsername(users.get(0).getFirstName() + " " + users.get(0).getLastName());
			booking.setMobile(users.get(0).getContact());

			bookings.add(booking);
		}

		response.setBookings(bookings);
		response.setResponseMessage("Booking fetched successfully...!!!");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BookingResponseDto> fetchBookingByUserId(int userId) {

		BookingResponseDto response = new BookingResponseDto();

		List<TrainBooking> trainBookings = this.trainBookingService.getByUserId(userId);

		if (trainBookings == null) {
			response.setResponseMessage("No Bookings made by users...!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		List<BookingResponse> bookings = new ArrayList<>();

		BookingResponse booking = null;

		for (TrainBooking trainBooking : trainBookings) {
			booking = new BookingResponse();

			booking.setBookId(trainBooking.getId());
			booking.setBookingId(trainBooking.getBookingId());
			booking.setBookingTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(trainBooking.getBookingTime()));
			booking.setUserId(trainBooking.getUserId());
			booking.setTrainId(trainBooking.getTrainId());
			booking.setTrainSeatId(trainBooking.getTrainSeatId());
			booking.setTrainSeat(trainBooking.getTrainSeat() != null ? trainBooking.getTrainSeat() : "---");
			booking.setScheduleTrainId(trainBooking.getScheduleTrainId());

			// hitting external service (TRAIN SERVICE)
			ScheduleTrainResponseDto scheduleTrainDetail = trainService
					.fetchScheduledTrainById(booking.getScheduleTrainId());
			TrainDetail train = scheduleTrainDetail.getTrain().get(0);

			booking.setTrainName(train.getName());
			booking.setTrainNumber(train.getNumber());
			booking.setFromLocation(train.getFromLocation());
			booking.setToLocation(train.getToLocation());
			booking.setSeatPrice(train.getSeatPrice());
			booking.setScheduleTrainTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(train.getScheduleTime()));
			booking.setStatus(BookingStatus.getStatusString(trainBooking.getStatus()));

			// calling user service to fetch User by using user id
			UserListResponseDto userResponse = userService.fetchUserById(trainBooking.getUserId());
			List<User> users = userResponse.getUsers();

			booking.setUsername(users.get(0).getFirstName() + " " + users.get(0).getLastName());
			booking.setMobile(users.get(0).getContact());

			bookings.add(booking);
		}

		response.setBookings(bookings);
		response.setResponseMessage("Booking fetched successfully...!!!");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BookingResponseDto> fetchBookingByScheduleTrainId(int scheduleTrainId) {

		BookingResponseDto response = new BookingResponseDto();

		List<TrainBooking> trainBookings = this.trainBookingService.getByScheduleTrainIdAndStatus(scheduleTrainId,
				BookingStatus.AVAILABLE.value());

		if (trainBookings == null) {
			response.setResponseMessage("No Bookings made by users...!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		List<BookingResponse> bookings = new ArrayList<>();

		BookingResponse booking = null;

		for (TrainBooking trainBooking : trainBookings) {
			booking = new BookingResponse();

			booking.setBookId(trainBooking.getId());
			booking.setBookingId(trainBooking.getBookingId());
			booking.setBookingTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(trainBooking.getBookingTime()));
			booking.setUserId(trainBooking.getUserId());
			booking.setTrainId(trainBooking.getTrainId());
			booking.setTrainSeatId(trainBooking.getTrainSeatId());
			booking.setScheduleTrainId(trainBooking.getScheduleTrainId());

			// hitting external service (TRAIN SERVICE)
			ScheduleTrainResponseDto scheduleTrainDetail = trainService
					.fetchScheduledTrainById(booking.getScheduleTrainId());
			TrainDetail train = scheduleTrainDetail.getTrain().get(0);
			List<TrainSeat> trainSeats = train.getTrainSeats();

			booking.setTrainName(train.getName());
			booking.setTrainNumber(train.getNumber());
			booking.setFromLocation(train.getFromLocation());
			booking.setToLocation(train.getToLocation());
			booking.setSeatPrice(train.getSeatPrice());
			booking.setScheduleTrainTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(train.getScheduleTime()));
			booking.setStatus(BookingStatus.getStatusString(trainBooking.getStatus()));

			for (TrainSeat trainSeat : trainSeats) {
				if (trainSeat.getId() == booking.getTrainSeatId()) {
					booking.setTrainSeat(trainSeat.getSeatNo());
					break;
				}
			}

			// calling user service to fetch User by using user id
			UserListResponseDto userResponse = userService.fetchUserById(trainBooking.getUserId());
			List<User> users = userResponse.getUsers();

			booking.setUsername(users.get(0).getFirstName() + " " + users.get(0).getLastName());
			booking.setMobile(users.get(0).getContact());

			bookings.add(booking);
		}

		response.setBookings(bookings);
		response.setResponseMessage("Booking fetched successfully...!!!");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BookingResponseDto> fetchScheduledTrainBookings(int scheduleTrainId) {

		BookingResponseDto response = new BookingResponseDto();

		// we just want to show validate Train Seat Tickets
		List<TrainBooking> trainBookings = this.trainBookingService.getByScheduleTrainIdAndStatusIn(scheduleTrainId,
				Arrays.asList(BookingStatus.AVAILABLE.value(), BookingStatus.CONFIRMED.value()));

		if (trainBookings == null) {
			response.setResponseMessage("No Bookings made by users...!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		List<BookingResponse> bookings = new ArrayList<>();

		BookingResponse booking = null;

		for (TrainBooking trainBooking : trainBookings) {
			booking = new BookingResponse();

			booking.setBookId(trainBooking.getId());
			booking.setBookingId((trainBooking.getBookingId() == null || trainBooking.getBookingId().equals("")) ? "---"
					: trainBooking.getBookingId());
			booking.setBookingTime(
					(trainBooking.getBookingTime() == null || trainBooking.getBookingTime().equals("")) ? "---"
							: DateTimeUtil.getProperDateTimeFormatFromEpochTime(trainBooking.getBookingTime()));
//			booking.setUserId(trainBooking.getUserId());
//			booking.setTrainId(trainBooking.getTrainId());
			booking.setTrainSeatId(trainBooking.getTrainSeatId());
			booking.setScheduleTrainId(trainBooking.getScheduleTrainId());

			// hitting external service (TRAIN SERVICE)
			ScheduleTrainResponseDto scheduleTrainDetail = trainService
					.fetchScheduledTrainById(booking.getScheduleTrainId());
			TrainDetail train = scheduleTrainDetail.getTrain().get(0);

//			booking.setTrainName(train.getName());
//			booking.setTrainNumber(train.getNumber());
//			booking.setFromLocation(train.getFromLocation());
//			booking.setToLocation(train.getToLocation());
			booking.setSeatPrice(train.getSeatPrice());
			booking.setScheduleTrainTime((train.getScheduleTime() == null || train.getScheduleTime().equals("")) ? "---"
					: DateTimeUtil.getProperDateTimeFormatFromEpochTime(train.getScheduleTime()));

			booking.setStatus(BookingStatus.getStatusString(trainBooking.getStatus()));
			booking.setTrainSeat(trainBooking.getTrainSeat());

//			// calling user service to fetch User by using user id
//			UserListResponseDto userResponse = userService.fetchUserById(trainBooking.getUserId());
//			List<User> users = userResponse.getUsers();
//
//			booking.setUsername(users.get(0).getFirstName() + " " + users.get(0).getLastName());
//			booking.setMobile(users.get(0).getContact());

			bookings.add(booking);
		}

		response.setBookings(bookings);
		response.setResponseMessage("Scheduled Train Tickets fetched successfully...!!!");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BookingTicketCountResponse> fetchScheduledTrainTicketCounts(int scheduleTrainId) {

		BookingTicketCountResponse response = new BookingTicketCountResponse();

		if (scheduleTrainId == 0) {
			response.setResponseMessage("Schedule train id missing...!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);

			return new ResponseEntity<BookingTicketCountResponse>(response, HttpStatus.BAD_REQUEST);
		}

		List<TrainBooking> availableTickets = new ArrayList<>();
		availableTickets = this.trainBookingService.getByScheduleTrainIdAndStatus(scheduleTrainId,
				BookingStatus.AVAILABLE.value());

		List<TrainBooking> confirmedTickets = new ArrayList<>();
		confirmedTickets = this.trainBookingService.getByScheduleTrainIdAndStatus(scheduleTrainId,
				BookingStatus.CONFIRMED.value());

		List<TrainBooking> waitingTickets = new ArrayList<>();
		waitingTickets = this.trainBookingService.getByScheduleTrainIdAndStatus(scheduleTrainId,
				BookingStatus.WAITING.value());

		response.setAvailableTicketCount(availableTickets.size());
		response.setWaitingTicketCount(waitingTickets.size());
		response.setConfirmedTicketCount(confirmedTickets.size());
		response.setResponseMessage("Train Tickets Count Fetched successfully...!!!");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<BookingTicketCountResponse>(response, HttpStatus.OK);

	}

	public void downloadBookingTicket(String bookingId, HttpServletResponse servletResponse) throws Exception {

		if (bookingId == null) {
			return;
		}

		List<TrainBooking> trainBookings = this.trainBookingService.getByBookingId(bookingId);

		if (trainBookings == null) {
			return;
		}

		List<BookingResponse> bookings = new ArrayList<>();

		BookingResponse booking = null;

		for (TrainBooking trainBooking : trainBookings) {
			booking = new BookingResponse();

			booking.setBookId(trainBooking.getId());
			booking.setBookingId(trainBooking.getBookingId());
			booking.setBookingTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(trainBooking.getBookingTime()));
			booking.setUserId(trainBooking.getUserId());
			booking.setTrainId(trainBooking.getTrainId());
			booking.setTrainSeatId(trainBooking.getTrainSeatId());
			booking.setTrainSeat(trainBooking.getTrainSeat());
			booking.setScheduleTrainId(trainBooking.getScheduleTrainId());

			// hitting external service (TRAIN SERVICE)
			ScheduleTrainResponseDto scheduleTrainDetail = trainService
					.fetchScheduledTrainById(booking.getScheduleTrainId());
			TrainDetail train = scheduleTrainDetail.getTrain().get(0);

			booking.setTrainName(train.getName());
			booking.setTrainNumber(train.getNumber());
			booking.setFromLocation(train.getFromLocation());
			booking.setToLocation(train.getToLocation());
			booking.setSeatPrice(train.getSeatPrice());
			booking.setScheduleTrainTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(train.getScheduleTime()));
			booking.setStatus(BookingStatus.getStatusString(trainBooking.getStatus()));

			// calling user service to fetch User by using user id
			UserListResponseDto userResponse = userService.fetchUserById(trainBooking.getUserId());
			List<User> users = userResponse.getUsers();

			booking.setUsername(users.get(0).getFirstName() + " " + users.get(0).getLastName());
			booking.setMobile(users.get(0).getContact());

			bookings.add(booking);
		}

		servletResponse.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + bookings.get(0).getUsername() + " "
				+ bookings.get(0).getBookingId() + "_Bill.pdf";
		servletResponse.setHeader(headerKey, headerValue);

		TicketDownloader exporter = new TicketDownloader(bookings);
		exporter.export(servletResponse);

		return;

	}

}
