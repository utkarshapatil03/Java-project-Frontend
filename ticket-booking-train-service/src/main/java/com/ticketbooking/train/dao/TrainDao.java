package com.ticketbooking.train.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketbooking.train.entity.Train;

@Repository
public interface TrainDao extends JpaRepository<Train, Integer> {

	Train findByNumber(String trainNumber);

	List<Train> findByNumberContainingIgnoreCaseAndStatus(String trainNumber, int status);

	List<Train> findByFromLocationIdAndToLocationIdAndStatus(int fromLocationId, int toLocationId, int status);

	List<Train> findByStatus(int status);
}
