package com.ticketbooking.book.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketbooking.book.entity.TrainBooking;

@Repository
public interface TrainBookingDao extends JpaRepository<TrainBooking, Integer> {

	List<TrainBooking> findByUserIdOrderByIdDesc(int userId);

	List<TrainBooking> findByUserIdAndStatusOrderByIdDesc(int userId, int status);

	List<TrainBooking> findByTrainIdOrderByIdDesc(int trainId);

	List<TrainBooking> findByTrainIdAndStatusOrderByIdDesc(int trainId, int status);

	List<TrainBooking> findByScheduleTrainIdAndStatusOrderByIdDesc(int scheduleTrainId, int status);

	List<TrainBooking> findByScheduleTrainId(int scheduleTrainId);

	List<TrainBooking> findByStatusOrderByIdDesc(int status);

	List<TrainBooking> findByBookingTimeBetweenAndUserIdOrderByIdDesc(String startDate, String endDate, int userId);

	List<TrainBooking> findByBookingTimeBetweenOrderByIdDesc(String startDate, String endDate);

	TrainBooking findByUserIdAndScheduleTrainIdAndTrainSeatIdAndStatusOrderByIdDesc(int userId, int scheduleTrainId,
			int trainSeatId, int status);

	List<TrainBooking> findAllByOrderByIdDesc();

	List<TrainBooking> findByStatusNotOrderByIdDesc(int status);

	List<TrainBooking> findByScheduleTrainIdAndStatusNotOrderByIdDesc(int scheduleTrainId, int status);

	List<TrainBooking> findByScheduleTrainIdAndStatusIn(int scheduleTrainId, List<Integer> statuses);
	
	List<TrainBooking> findByBookingId(String bookingId);

}
