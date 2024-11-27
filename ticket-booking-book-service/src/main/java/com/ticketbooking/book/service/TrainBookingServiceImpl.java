package com.ticketbooking.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketbooking.book.dao.TrainBookingDao;
import com.ticketbooking.book.entity.TrainBooking;

@Service
public class TrainBookingServiceImpl implements TrainBookingService {

	@Autowired
	private TrainBookingDao trainBookingDao;

	@Override
	public TrainBooking bookTrain(TrainBooking booking) {
		// TODO Auto-generated method stub
		return trainBookingDao.save(booking);
	}

	@Override
	public TrainBooking updateBooking(TrainBooking booking) {
		// TODO Auto-generated method stub
		return trainBookingDao.save(booking);
	}

	@Override
	public TrainBooking getTrainBookingById(int id) {
		// TODO Auto-generated method stub
		return trainBookingDao.findById(id).get();
	}

	@Override
	public List<TrainBooking> getAllTrainBookingsByStatus(int status) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByStatusOrderByIdDesc(status);
	}

	@Override
	public List<TrainBooking> getAllTrainBookings() {
		// TODO Auto-generated method stub
		return trainBookingDao.findAllByOrderByIdDesc();
	}

	@Override
	public List<TrainBooking> getByUserId(int userId) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByUserIdOrderByIdDesc(userId);
	}

	@Override
	public List<TrainBooking> getByUserIdandStatus(int userId, int status) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByUserIdAndStatusOrderByIdDesc(userId, status);
	}

	@Override
	public List<TrainBooking> getByTrainId(int trainId) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByTrainIdOrderByIdDesc(trainId);
	}

	@Override
	public List<TrainBooking> getByTrainIdAndStatus(int trainId, int status) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByTrainIdAndStatusOrderByIdDesc(trainId, status);
	}

	@Override
	public List<TrainBooking> getByScheduleTrainIdAndStatus(int scheduleTrainId, int status) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByScheduleTrainIdAndStatusOrderByIdDesc(scheduleTrainId, status);
	}

	@Override
	public List<TrainBooking> getByScheduleTrainId(int scheduleTrainId) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByScheduleTrainId(scheduleTrainId);
	}

	@Override
	public List<TrainBooking> getByBookingTimeBetweenAndUserId(String startDate, String endDate, int userId) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByBookingTimeBetweenAndUserIdOrderByIdDesc(startDate, endDate, userId);
	}

	@Override
	public List<TrainBooking> getByBookingTimeBetween(String startDate, String endDate) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByBookingTimeBetweenOrderByIdDesc(startDate, endDate);
	}

	@Override
	public TrainBooking getByUserIdAndScheduleTrainIdAndTrainSeatIdAndStatus(int userId, int scheduleTrainId,
			int trainSeatId, int status) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByUserIdAndScheduleTrainIdAndTrainSeatIdAndStatusOrderByIdDesc(userId, scheduleTrainId, trainSeatId, status);
	}

	@Override
	public List<TrainBooking> getByStatusNotOrderByIdDesc(int status) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByStatusNotOrderByIdDesc(status);
	}

	@Override
	public List<TrainBooking> getByScheduleTrainIdAndStatusNotOrderByIdDesc(int scheduleTrainId, int status) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByScheduleTrainIdAndStatusNotOrderByIdDesc(scheduleTrainId, status);
	}

	@Override
	public List<TrainBooking> getByScheduleTrainIdAndStatusIn(int scheduleTrainId, List<Integer> statuses) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByScheduleTrainIdAndStatusIn(scheduleTrainId,statuses);
	}

	@Override
	public List<TrainBooking> getByBookingId(String bookingId) {
		// TODO Auto-generated method stub
		return trainBookingDao.findByBookingId(bookingId);
	}


}
