package com.ticketbooking.train.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketbooking.train.entity.TrainSeat;

@Repository
public interface TrainSeatDao extends JpaRepository<TrainSeat, Integer> {

	List<TrainSeat> findByTrainId(int trainId);

}
