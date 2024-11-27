package com.ticketbooking.train.utility;

import java.util.ArrayList;
import java.util.List;

import com.ticketbooking.train.entity.TrainSeat;

public class TrainSeatGenerator {

	public static List<TrainSeat> generateTrainSeat(int totalCoaches, int seatsPerCoach, int trainId) {
		List<TrainSeat> trainSeats = new ArrayList<>();
		int startAscii = 65; // ASCII value of 'A'

		for (int i = 0; i < totalCoaches; i++) {
			char coachLabel = (char) (startAscii + i);

			for (int j = 1; j <= seatsPerCoach; j++) {
				String seatLabel = coachLabel + String.valueOf(j);

				TrainSeat seat = new TrainSeat();
				seat.setSeatNo(seatLabel);
				seat.setTrainId(trainId);

				trainSeats.add(seat);
			}
		}

		return trainSeats;
	}

}
