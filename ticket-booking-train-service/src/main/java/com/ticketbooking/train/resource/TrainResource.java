package com.ticketbooking.train.resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbooking.train.constants.DatabaseConstant.TrainStatus;
import com.ticketbooking.train.dto.AddScheduleTrainTicketRequestDto;
import com.ticketbooking.train.dto.CommonApiResponse;
import com.ticketbooking.train.dto.LocationResponseDto;
import com.ticketbooking.train.dto.ScheduleTrainResponseDto;
import com.ticketbooking.train.dto.TrainDetail;
import com.ticketbooking.train.dto.TrainDetailResponseDto;
import com.ticketbooking.train.entity.ScheduleTrain;
import com.ticketbooking.train.entity.Train;
import com.ticketbooking.train.entity.TrainSeat;
import com.ticketbooking.train.exception.TrainBookingAddTicketsFailedException;
import com.ticketbooking.train.external.TrainBookingService;
import com.ticketbooking.train.external.TrainLocationService;
import com.ticketbooking.train.service.ScheduleTrainService;
import com.ticketbooking.train.service.TrainSeatService;
import com.ticketbooking.train.service.TrainService;
import com.ticketbooking.train.utility.DateTimeUtil;
import com.ticketbooking.train.utility.ScheduleTrainReferenceNumberGenerator;
import com.ticketbooking.train.utility.TrainSeatGenerator;

@Component
public class TrainResource {

	@Autowired
	private TrainService trainService;

	@Autowired
	private TrainSeatService trainSeatService;

	@Autowired
	private ScheduleTrainService scheduleTrainService;

	@Autowired
	private TrainLocationService trainLocationService;
	
	@Autowired
	private TrainBookingService trainBookingService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Transactional
	public ResponseEntity<CommonApiResponse> addTrain(Train train) {

		CommonApiResponse response = new CommonApiResponse();

		if (train == null) {
			response.setResponseMessage("Bad Request, Train details not proper");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Train existingTrain = trainService.getTrainByNumber(train.getNumber());

		if (existingTrain != null) {
			response.setResponseMessage("Train already exist with this Number");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		train.setStatus(TrainStatus.ACTIVE.value());

		// adding train in database
		existingTrain = this.trainService.addTrain(train);

		if (existingTrain == null) {
			response.setResponseMessage("failed to register train");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		// adding train seat in database

		List<TrainSeat> trainSeats = TrainSeatGenerator.generateTrainSeat(train.getTotalCoach(),
				train.getTotalSeatInEachCoach(), existingTrain.getId());

		List<TrainSeat> addedTrainSeats = trainSeatService.addTrainSeats(trainSeats);

		if (addedTrainSeats == null) {
			System.out.println("Failed to add train seats");
		}

		response.setResponseMessage("Train registered Successfully");
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

	public ResponseEntity<CommonApiResponse> updateTrain(Train train) {

		CommonApiResponse response = new CommonApiResponse();

		if (train == null) {
			response.setResponseMessage("Bad Request, Train details not proper");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Train existingTrain = trainService.getTrainById(train.getId());

		if (existingTrain == null) {
			response.setResponseMessage("Train not found in database");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		// during update we can edit only 3 things
		existingTrain.setName(train.getName());
		existingTrain.setNumber(train.getNumber());
		existingTrain.setSeatPrice(train.getSeatPrice());

		// adding train in database
		Train updatedTrain = this.trainService.updateTrain(existingTrain);

		if (updatedTrain == null) {
			response.setResponseMessage("failed to update train");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Train updated Successfully");
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

	public ResponseEntity<CommonApiResponse> deleteTrain(int trainId) {

		CommonApiResponse response = new CommonApiResponse();

		if (trainId == 0) {
			response.setResponseMessage("Bad request, Train Id is 0");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Train train = this.trainService.getTrainById(trainId);

		if (train == null) {
			response.setResponseMessage("Train not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		train.setStatus(TrainStatus.NOT_ACTIVE.value());

		Train deletedTrain = this.trainService.updateTrain(train);

		if (deletedTrain == null) {
			response.setResponseMessage("Failed to delete the Train");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Train deleted Successfully");
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

	public ResponseEntity<TrainDetailResponseDto> fetchTrainDetail(int trainId) {

		TrainDetailResponseDto response = new TrainDetailResponseDto();

		if (trainId == 0) {
			response.setResponseMessage("Bad request, Train Id is 0");
			response.setSuccess(true);

			return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Train train = this.trainService.getTrainById(trainId);

		if (train == null) {
			response.setResponseMessage("Train not found");
			response.setSuccess(true);

			return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		TrainDetail trainDetail = new TrainDetail();
		trainDetail.setId(train.getId());
		trainDetail.setName(train.getName());
		trainDetail.setNumber(train.getNumber());
		trainDetail.setSeatPrice(train.getSeatPrice());
		trainDetail.setTotalCoach(train.getTotalCoach());
		trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());

		// hitting location service for fetching location using id
		LocationResponseDto fromLocation = this.trainLocationService.fetchLocationById(train.getFromLocationId());
		LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

		trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
		trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

		response.setTrain(Arrays.asList(trainDetail));
		response.setResponseMessage("Train Fetched Successfully");
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

		return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<TrainDetailResponseDto> fetchAllTrains() {

		TrainDetailResponseDto response = new TrainDetailResponseDto();

		List<TrainDetail> trainDetails = new ArrayList<>();

		List<Train> trains = this.trainService.getTrainByStatus(TrainStatus.ACTIVE.value());

		if (trains == null) {
			response.setResponseMessage("No Active Trains found in database");
			response.setSuccess(true);

			return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.OK);
		}

		for (Train train : trains) {
			TrainDetail trainDetail = new TrainDetail();
			trainDetail.setId(train.getId());
			trainDetail.setName(train.getName());
			trainDetail.setNumber(train.getNumber());
			trainDetail.setSeatPrice(train.getSeatPrice());
			trainDetail.setTotalCoach(train.getTotalCoach());
			trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());

			// hitting location service for fetching location using id
			LocationResponseDto fromLocation = this.trainLocationService.fetchLocationById(train.getFromLocationId());
			LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

			trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
			trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

			trainDetails.add(trainDetail);
		}

		response.setTrain(trainDetails);
		response.setResponseMessage("Train Fetched Successfully");
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

		return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<TrainDetailResponseDto> fetchTrainDetailsFromAndToLocation(int fromLocationId,
			int toLocationId) {

		TrainDetailResponseDto response = new TrainDetailResponseDto();

		if (fromLocationId == 0 || toLocationId == 0) {
			response.setResponseMessage("Bad request, Location not selected");
			response.setSuccess(true);

			return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Train> trains = this.trainService.getTrainByFromAndToLocation(fromLocationId, toLocationId,
				TrainStatus.ACTIVE.value());

		if (trains == null) {
			response.setResponseMessage("Trains not found");
			response.setSuccess(true);

			return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<TrainDetail> trainDetails = new ArrayList<>();

		// hitting location service for fetching location using id
		LocationResponseDto fromLocation = this.trainLocationService.fetchLocationById(fromLocationId);
		LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(toLocationId);

		for (Train train : trains) {
			TrainDetail trainDetail = new TrainDetail();
			trainDetail.setId(train.getId());
			trainDetail.setName(train.getName());
			trainDetail.setNumber(train.getNumber());
			trainDetail.setSeatPrice(train.getSeatPrice());
			trainDetail.setTotalCoach(train.getTotalCoach());
			trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
			trainDetail.setToLocation(toLocation.getLocation().get(0).getName());
			trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());

			trainDetails.add(trainDetail);
		}

		response.setTrain(trainDetails);
		response.setResponseMessage("Train Fetched Successfully");
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

		return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<TrainDetailResponseDto> searchTrain(String trainNumber) {

		TrainDetailResponseDto response = new TrainDetailResponseDto();

		if (trainNumber == null) {
			response.setResponseMessage("Bad request, trainNumber found empty");
			response.setSuccess(true);

			return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Train> trains = this.trainService.getAllTrainsByNumber(trainNumber, TrainStatus.ACTIVE.value());

		if (trains == null) {
			response.setResponseMessage("Trains not found");
			response.setSuccess(true);

			return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<TrainDetail> trainDetails = new ArrayList<>();

		for (Train train : trains) {
			TrainDetail trainDetail = new TrainDetail();
			trainDetail.setId(train.getId());
			trainDetail.setName(train.getName());
			trainDetail.setNumber(train.getNumber());
			trainDetail.setSeatPrice(train.getSeatPrice());
			trainDetail.setTotalCoach(train.getTotalCoach());
			trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());

			// hitting location service for fetching location using id
			LocationResponseDto fromLocation = this.trainLocationService.fetchLocationById(train.getFromLocationId());
			LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

			trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
			trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

			trainDetails.add(trainDetail);
		}

		response.setTrain(trainDetails);
		response.setResponseMessage("Train Fetched Successfully");
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

		return new ResponseEntity<TrainDetailResponseDto>(response, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<CommonApiResponse> scheduleTrain(ScheduleTrain scheduleTrain) {

		CommonApiResponse response = new CommonApiResponse();

		if (scheduleTrain == null) {
			response.setResponseMessage("Bad Request, Schedule data not proper");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (scheduleTrain.getTrainId() == 0) {
			response.setResponseMessage("Bad Request, Train not selected");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Train train = this.trainService.getTrainById(scheduleTrain.getTrainId());

		if (train == null || train.getStatus() != TrainStatus.ACTIVE.value()) {
			response.setResponseMessage("Bad Request, Train not Active");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (scheduleTrain.getScheduleTime() == null) {
			response.setResponseMessage("Bad Request, Schedule time not proper");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		scheduleTrain.setStatus(TrainStatus.ACTIVE.value());
		scheduleTrain.setScheduleTrainId(ScheduleTrainReferenceNumberGenerator.generate());

		ScheduleTrain scheduledTrain = this.scheduleTrainService.scheduleTrain(scheduleTrain);

		if (scheduledTrain != null) {
			
			// add train seats in Booking during Train schedule only
             
			List<TrainSeat> trainSeats = new ArrayList<>();
			trainSeats = this.trainSeatService.getTrainSeatByTrainId(train.getId());
			
			AddScheduleTrainTicketRequestDto addScheduleTrainTicketRequestDto = new AddScheduleTrainTicketRequestDto();
			addScheduleTrainTicketRequestDto.setTrainSeats(trainSeats);
			addScheduleTrainTicketRequestDto.setScheduleTrainId(scheduledTrain.getId());
			
			ResponseEntity<CommonApiResponse> addTicketResponse = this.trainBookingService.addScheduledTrainTickets(addScheduleTrainTicketRequestDto);
			
			if(addTicketResponse == null) {
				throw new TrainBookingAddTicketsFailedException("Failed to add Scheduled Train Tickets in Booking Table...!!!");
			} 
			
			response.setResponseMessage("Train Successfully Scheduled..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);

		}

		else {
			response.setResponseMessage("Failed to schedule Train..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

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

	public ResponseEntity<ScheduleTrainResponseDto> getScheduledTrainById(int scheduleTrainId) {

		ScheduleTrainResponseDto response = new ScheduleTrainResponseDto();

		if (scheduleTrainId == 0) {
			response.setResponseMessage("Bad Request, Schedule Train Id is missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		ScheduleTrain scheduleTrain = this.scheduleTrainService.getByScheduleTrainId(scheduleTrainId);

		if (scheduleTrain == null) {
			response.setResponseMessage("Bad Request, Schedule train not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Train train = this.trainService.getTrainById(scheduleTrain.getTrainId());

		if (train == null) {
			response.setResponseMessage("Bad Request, train not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<TrainDetail> trainDetails = new ArrayList<>();

		List<TrainSeat> trainSeats = this.trainSeatService.getTrainSeatByTrainId(train.getId());

		if (trainSeats == null) {
			response.setResponseMessage("Bad Request, trainSeats not found in database");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		TrainDetail trainDetail = new TrainDetail();
		trainDetail.setId(train.getId());
		trainDetail.setName(train.getName());
		trainDetail.setNumber(train.getNumber());
		trainDetail.setSeatPrice(train.getSeatPrice());
		trainDetail.setTotalCoach(train.getTotalCoach());
		trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
		trainDetail.setScheduleTime(scheduleTrain.getScheduleTime());
		trainDetail.setScheduleTrainId(scheduleTrain.getScheduleTrainId());
		trainDetail.setTrainSeats(trainSeats);

		// hitting location service for fetching location using id
		LocationResponseDto fromLocation = this.trainLocationService.fetchLocationById(train.getFromLocationId());
		LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

		trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
		trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

		trainDetails.add(trainDetail);

		response.setTrain(trainDetails);
		response.setResponseMessage("Scheduled Train fetched success..!!!");
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

		return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> deleteScheduleTrain(String scheduleTrainId) {

		CommonApiResponse response = new CommonApiResponse();

		if (scheduleTrainId == null) {
			response.setResponseMessage("Bad Request, scheduleTrainId not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		ScheduleTrain scheduledTrain = this.scheduleTrainService.getByScheduleTrainId(scheduleTrainId);

		if (scheduledTrain == null || scheduledTrain.getStatus() != TrainStatus.ACTIVE.value()) {
			response.setResponseMessage("Bad Request, Scheduled Train is already In-Active");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		scheduledTrain.setStatus(TrainStatus.NOT_ACTIVE.value());

		ScheduleTrain deletedTrain = this.scheduleTrainService.scheduleTrain(scheduledTrain);

		if (deletedTrain != null) {
			response.setResponseMessage("Deleted Scheduled Train..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to delete schedule Train..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

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

	public ResponseEntity<ScheduleTrainResponseDto> searchScheduledTrainByTime(String startTime, String endTime) {

		ScheduleTrainResponseDto response = new ScheduleTrainResponseDto();

		if (startTime == null || endTime == null) {
			response.setResponseMessage("Bad Request, start time or end time missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleTrain> scheduledTrains = new ArrayList<>();

		scheduledTrains = this.scheduleTrainService.getByScheduleTimeBetweenAndStatus(startTime, endTime,
				TrainStatus.ACTIVE.value());

		List<TrainDetail> trainDetails = new ArrayList<>();

		if (!scheduledTrains.isEmpty()) {

			for (ScheduleTrain scheduleTrain : scheduledTrains) {
				Train train = this.trainService.getTrainById(scheduleTrain.getTrainId());

				TrainDetail trainDetail = new TrainDetail();
				trainDetail.setId(train.getId());
				trainDetail.setName(train.getName());
				trainDetail.setNumber(train.getNumber());
				trainDetail.setSeatPrice(train.getSeatPrice());
				trainDetail.setTotalCoach(train.getTotalCoach());
				trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
				trainDetail.setScheduleTime(scheduleTrain.getScheduleTime());
				trainDetail.setScheduleTrainId(scheduleTrain.getScheduleTrainId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.trainLocationService
						.fetchLocationById(train.getFromLocationId());
				LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

				trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

				trainDetails.add(trainDetail);
			}

			response.setTrain(trainDetails);
			response.setResponseMessage("Scheduled Train fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to schedule Train..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleTrainResponseDto> fetchedScheduledTrain() {

		ScheduleTrainResponseDto response = new ScheduleTrainResponseDto();

		String startTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		if (startTime == null) {
			response.setResponseMessage("Bad Request, start time missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleTrain> scheduledTrains = new ArrayList<>();

		scheduledTrains = this.scheduleTrainService.getByScheduleTimeGreaterThanEqualAndStatus(startTime,
				TrainStatus.ACTIVE.value());

		List<TrainDetail> trainDetails = new ArrayList<>();

		if (!scheduledTrains.isEmpty()) {

			for (ScheduleTrain scheduleTrain : scheduledTrains) {
				Train train = this.trainService.getTrainById(scheduleTrain.getTrainId());

				TrainDetail trainDetail = new TrainDetail();
				trainDetail.setId(train.getId());
				trainDetail.setName(train.getName());
				trainDetail.setNumber(train.getNumber());
				trainDetail.setSeatPrice(train.getSeatPrice());
				trainDetail.setTotalCoach(train.getTotalCoach());
				trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
				trainDetail.setScheduleTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(scheduleTrain.getScheduleTime()));
				trainDetail.setScheduleTrainId(scheduleTrain.getScheduleTrainId());
                trainDetail.setScheduleId(scheduleTrain.getId());
				
				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.trainLocationService
						.fetchLocationById(train.getFromLocationId());
				LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

				trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

				trainDetails.add(trainDetail);
			}

		}

		response.setTrain(trainDetails);
		response.setResponseMessage("Scheduled Trains Fetched Successfully");
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

		return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleTrainResponseDto> fetchScheduledTrainByTime(String startTime, int trainId) {

		ScheduleTrainResponseDto response = new ScheduleTrainResponseDto();

		if (startTime == null || trainId == 0) {
			response.setResponseMessage("Bad Request, start time or train id missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Train train = this.trainService.getTrainById(trainId);

		if (train == null) {
			response.setResponseMessage("Bad Request, train not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleTrain> scheduledTrains = new ArrayList<>();

		scheduledTrains = this.scheduleTrainService.getByScheduleTimeGreaterThanEqualAndStatusAndTrainId(startTime,
				TrainStatus.ACTIVE.value(), trainId);

		List<TrainDetail> trainDetails = new ArrayList<>();

		if (!scheduledTrains.isEmpty()) {

			for (ScheduleTrain scheduleTrain : scheduledTrains) {

				TrainDetail trainDetail = new TrainDetail();
				trainDetail.setId(train.getId());
				trainDetail.setName(train.getName());
				trainDetail.setNumber(train.getNumber());
				trainDetail.setSeatPrice(train.getSeatPrice());
				trainDetail.setTotalCoach(train.getTotalCoach());
				trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
				trainDetail.setScheduleTime(scheduleTrain.getScheduleTime());
				trainDetail.setScheduleTrainId(scheduleTrain.getScheduleTrainId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.trainLocationService
						.fetchLocationById(train.getFromLocationId());
				LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

				trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

				trainDetails.add(trainDetail);
			}

			response.setTrain(trainDetails);
			response.setResponseMessage("Scheduled Train fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to schedule Train..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleTrainResponseDto> fetchScheduledTrainByTimeRange(String startTime, String endTime,
			int trainId) {

		ScheduleTrainResponseDto response = new ScheduleTrainResponseDto();

		if (startTime == null || startTime == null || trainId == 0) {
			response.setResponseMessage("Bad Request, time or train id missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Train train = this.trainService.getTrainById(trainId);

		if (train == null) {
			response.setResponseMessage("Bad Request, train not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleTrain> scheduledTrains = new ArrayList<>();

		scheduledTrains = this.scheduleTrainService.getByScheduleTimeBetweenAndStatusAndTrainId(startTime, endTime,
				TrainStatus.ACTIVE.value(), trainId);

		List<TrainDetail> trainDetails = new ArrayList<>();

		if (!scheduledTrains.isEmpty()) {

			for (ScheduleTrain scheduleTrain : scheduledTrains) {

				TrainDetail trainDetail = new TrainDetail();
				trainDetail.setId(train.getId());
				trainDetail.setName(train.getName());
				trainDetail.setNumber(train.getNumber());
				trainDetail.setSeatPrice(train.getSeatPrice());
				trainDetail.setTotalCoach(train.getTotalCoach());
				trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
				trainDetail.setScheduleTime(scheduleTrain.getScheduleTime());
				trainDetail.setScheduleTrainId(scheduleTrain.getScheduleTrainId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.trainLocationService
						.fetchLocationById(train.getFromLocationId());
				LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

				trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

				trainDetails.add(trainDetail);
			}

			response.setTrain(trainDetails);
			response.setResponseMessage("Scheduled Train fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to schedule Train..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleTrainResponseDto> fetchScheduledTrainsByLocationsAndCurrentTime(int sourceLocationId,
			int destinationLocationId) {

		ScheduleTrainResponseDto response = new ScheduleTrainResponseDto();

		List<Integer> trainIds = new ArrayList<>();

		if (sourceLocationId == 0 || destinationLocationId == 0) {
			response.setResponseMessage("Bad request, Location not selected");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Train> trains = this.trainService.getTrainByFromAndToLocation(sourceLocationId, destinationLocationId,
				TrainStatus.ACTIVE.value());

		if (trains == null) {
			response.setResponseMessage("Trains not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		for (Train train : trains) {
			trainIds.add(train.getId());
		}

		String currentTimeInEpoch = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<ScheduleTrain> scheduledTrains = this.scheduleTrainService
				.getByScheduleTimeGreaterThanEqualAndTrainIdInAndStatus(currentTimeInEpoch, trainIds,
						TrainStatus.ACTIVE.value());

		List<TrainDetail> trainDetails = new ArrayList<>();

		if (!scheduledTrains.isEmpty()) {

			for (ScheduleTrain scheduleTrain : scheduledTrains) {
				Train train = this.trainService.getTrainById(scheduleTrain.getTrainId());

				TrainDetail trainDetail = new TrainDetail();
				trainDetail.setId(train.getId());
				trainDetail.setName(train.getName());
				trainDetail.setNumber(train.getNumber());
				trainDetail.setSeatPrice(train.getSeatPrice());
				trainDetail.setTotalCoach(train.getTotalCoach());
				trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
				trainDetail.setScheduleTime(
						DateTimeUtil.getProperDateTimeFormatFromEpochTime(scheduleTrain.getScheduleTime()));
				trainDetail.setScheduleTrainId(scheduleTrain.getScheduleTrainId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.trainLocationService
						.fetchLocationById(train.getFromLocationId());
				LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

				trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

				trainDetails.add(trainDetail);
			}

			response.setTrain(trainDetails);
			response.setResponseMessage("Scheduled Train fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("No Scheduled train found");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<ScheduleTrainResponseDto> searchScheduledTrain(String startDate, String endDate,
			int sourceLocationId, int destinationLocationId) {
		
		ScheduleTrainResponseDto response = new ScheduleTrainResponseDto();
		
		if (startDate == null || endDate == null || sourceLocationId == 0 || destinationLocationId == 0) {
			response.setResponseMessage("Missing search input!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.BAD_REQUEST);
		}

		List<Integer> trainIds = new ArrayList<>();

		List<Train> trains = this.trainService.getTrainByFromAndToLocation(sourceLocationId, destinationLocationId,
				TrainStatus.ACTIVE.value());

		if (trains == null) {
			response.setResponseMessage("Trains not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		for (Train train : trains) {
			trainIds.add(train.getId());
		}

		List<ScheduleTrain> scheduledTrains = this.scheduleTrainService
				.getByScheduleTimeBetweenAndStatusAndTrainIdIn(startDate, endDate, TrainStatus.ACTIVE.value(), trainIds);

		List<TrainDetail> trainDetails = new ArrayList<>();

		if (!scheduledTrains.isEmpty()) {

			for (ScheduleTrain scheduleTrain : scheduledTrains) {
				Train train = this.trainService.getTrainById(scheduleTrain.getTrainId());

				TrainDetail trainDetail = new TrainDetail();
				trainDetail.setId(train.getId());
				trainDetail.setName(train.getName());
				trainDetail.setNumber(train.getNumber());
				trainDetail.setSeatPrice(train.getSeatPrice());
				trainDetail.setTotalCoach(train.getTotalCoach());
				trainDetail.setTotalSeatInEachCoach(train.getTotalSeatInEachCoach());
				trainDetail.setScheduleTime(
						DateTimeUtil.getProperDateTimeFormatFromEpochTime(scheduleTrain.getScheduleTime()));
				trainDetail.setScheduleTrainId(scheduleTrain.getScheduleTrainId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.trainLocationService
						.fetchLocationById(train.getFromLocationId());
				LocationResponseDto toLocation = this.trainLocationService.fetchLocationById(train.getToLocationId());

				trainDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				trainDetail.setToLocation(toLocation.getLocation().get(0).getName());

				trainDetails.add(trainDetail);
			}

			response.setResponseMessage("Scheduled Train fetched success..!!!");
		}

		else {
			response.setResponseMessage("No Scheduled train found");
		}
		
		response.setTrain(trainDetails);
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

		return new ResponseEntity<ScheduleTrainResponseDto>(response, HttpStatus.OK);
	
	}

}
