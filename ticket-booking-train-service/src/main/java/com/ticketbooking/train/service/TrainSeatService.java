package com.ticketbooking.train.service;

import java.util.List;

import com.ticketbooking.train.entity.TrainSeat;

public interface TrainSeatService {

	List<TrainSeat> addTrainSeats(List<TrainSeat> seats);

	List<TrainSeat> getTrainSeatByTrainId(int trainId);

}
