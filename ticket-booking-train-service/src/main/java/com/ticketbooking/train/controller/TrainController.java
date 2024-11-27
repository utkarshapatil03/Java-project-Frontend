package com.ticketbooking.train.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketbooking.train.dto.CommonApiResponse;
import com.ticketbooking.train.dto.ScheduleTrainResponseDto;
import com.ticketbooking.train.dto.TrainDetailResponseDto;
import com.ticketbooking.train.entity.ScheduleTrain;
import com.ticketbooking.train.entity.Train;
import com.ticketbooking.train.resource.TrainResource;

@RestController
@RequestMapping("/api/train/")
@CrossOrigin(origins = "http://localhost:3000")
public class TrainController {

	@Autowired
	private TrainResource trainResource;

	@PostMapping("/register")
	public ResponseEntity<CommonApiResponse> registerTrain(@RequestBody Train train) {
		return trainResource.addTrain(train);
	}

	@PutMapping("/update")
	public ResponseEntity<CommonApiResponse> updateTrain(@RequestBody Train train) {
		return trainResource.addTrain(train);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<CommonApiResponse> deleteTrain(@RequestParam("trainId") int trainId) {
		return trainResource.deleteTrain(trainId);
	}

	@GetMapping("/fetch")
	public ResponseEntity<TrainDetailResponseDto> fetchTrainDetail(@RequestParam("trainId") int trainId) {
		return trainResource.fetchTrainDetail(trainId);
	}
	
	@GetMapping("/fetch/all")
	public ResponseEntity<TrainDetailResponseDto> fetchAllTrains() {
		return trainResource.fetchAllTrains();
	}

	@GetMapping("/fetch/location/search")
	public ResponseEntity<TrainDetailResponseDto> fetchTrainDetailsFromAndToLocation(
			@RequestParam("fromLocationId") int fromLocationId, @RequestParam("toLocationId") int toLocationId) {
		return trainResource.fetchTrainDetailsFromAndToLocation(fromLocationId, toLocationId);
	}

	@GetMapping("/search/trainNumber")
	public ResponseEntity<TrainDetailResponseDto> searchTrain(@RequestParam("trainNo") String trainNumber) {
		return trainResource.searchTrain(trainNumber);
	}

	@PostMapping("/schedule")
	public ResponseEntity<CommonApiResponse> scheduleTrain(@RequestBody ScheduleTrain scheduleTrain) {
		return trainResource.scheduleTrain(scheduleTrain);
	}

	@GetMapping("/schedule/fetch/id")
	public ResponseEntity<ScheduleTrainResponseDto> fetchScheduledTrainById(
			@RequestParam("scheduleTrainId") int scheduleTrainId) {
		return trainResource.getScheduledTrainById(scheduleTrainId);
	}

	@DeleteMapping("/schedule")
	public ResponseEntity<CommonApiResponse> deleteScheduledTrain(
			@RequestParam("scheduleTrainId") String scheduleTrainId) {
		return trainResource.deleteScheduleTrain(scheduleTrainId);
	}

	// search scheduled trains by using time range
	// for all train
	@GetMapping("/scheduled/search/train/all/time")
	public ResponseEntity<ScheduleTrainResponseDto> searchScheduleTrain(@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime) {
		return trainResource.searchScheduledTrainByTime(startTime, endTime);
	}

	// search scheduled trains from current time
	// for all train
	@GetMapping("/scheduled/trains/all")
	public ResponseEntity<ScheduleTrainResponseDto> fecthedScheduledTrain() {
		return trainResource.fetchedScheduledTrain();
	}

	// search scheduled train from current time
	// for single train
	@GetMapping("/search/train/time")
	public ResponseEntity<ScheduleTrainResponseDto> fetchScheduledTrainByTime(
			@RequestParam("startTime") String startTime, @RequestParam("trainId") int trainId) {
		return trainResource.fetchScheduledTrainByTime(startTime, trainId);
	}

	// search scheduled train by using time range
	// for single train
	@GetMapping("/search/train/time/range")
	public ResponseEntity<ScheduleTrainResponseDto> fetchScheduledTrainByStartEndTime(
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam("trainId") int trainId) {
		return trainResource.fetchScheduledTrainByTimeRange(startTime, endTime, trainId);
	}

	// search scheduled train by source and destination location
	@GetMapping("/search/train/location")
	public ResponseEntity<ScheduleTrainResponseDto> fetchScheduledTrainByLocations(
			@RequestParam("sourceLocationId") int sourceLocationId, @RequestParam("destinationLocationId") int destinationLocationId) {
		return trainResource.fetchScheduledTrainsByLocationsAndCurrentTime(sourceLocationId, destinationLocationId);
	}
	
	@GetMapping("/serach/scheduled/traind")
	public ResponseEntity<ScheduleTrainResponseDto> searchScheduledTrain(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("sourceLocationId") int sourceLocationId, @RequestParam("destinationLocationId") int destinationLocationId) {
		return trainResource.searchScheduledTrain(startDate, endDate, sourceLocationId, destinationLocationId);
	}

}
