package com.ticketbooking.book.service;

import java.util.List;

import com.ticketbooking.book.entity.TrainBooking;

public interface TrainBookingService {
	
	TrainBooking bookTrain(TrainBooking booking);
	TrainBooking updateBooking(TrainBooking booking);
	TrainBooking getTrainBookingById(int id);
	List<TrainBooking> getAllTrainBookingsByStatus(int status);
	List<TrainBooking> getAllTrainBookings();
	List<TrainBooking> getByUserId(int userId);
	List<TrainBooking> getByUserIdandStatus(int userId, int status);
	List<TrainBooking> getByTrainId(int trainId);
	List<TrainBooking> getByTrainIdAndStatus(int trainId, int status);
	List<TrainBooking> getByScheduleTrainIdAndStatus(int scheduleTrainId, int status);
	List<TrainBooking> getByScheduleTrainId(int scheduleTrainId);
	List<TrainBooking> getByBookingTimeBetweenAndUserId(String startDate, String endDate, int userId);
	List<TrainBooking> getByBookingTimeBetween(String startDate, String endDate);
	TrainBooking getByUserIdAndScheduleTrainIdAndTrainSeatIdAndStatus(int userId, int scheduleTrainId, int trainSeatId, int status);
	List<TrainBooking> getByStatusNotOrderByIdDesc(int status);
	List<TrainBooking> getByScheduleTrainIdAndStatusNotOrderByIdDesc(int scheduleTrainId, int status);
	List<TrainBooking> getByScheduleTrainIdAndStatusIn(int scheduleTrainId, List<Integer> statuses);	
	List<TrainBooking> getByBookingId(String bookingId);
}
