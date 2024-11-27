package com.ticketbooking.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketbooking.train.dao.TrainSeatDao;
import com.ticketbooking.train.entity.TrainSeat;

@Service
public class TrainSeatIImpl implements TrainSeatService {
	
	@Autowired
	private TrainSeatDao trainSeatDao;

	@Override
	public List<TrainSeat> addTrainSeats(List<TrainSeat> seats) {
		return trainSeatDao.saveAll(seats);
	}

	@Override
	public List<TrainSeat> getTrainSeatByTrainId(int trainId) {
		return trainSeatDao.findByTrainId(trainId);
	}

}
