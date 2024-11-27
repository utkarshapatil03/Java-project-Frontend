package com.ticketbooking.book.dto;

public class TrainSeat {

	private int id;

	private int trainId;

	private String seatNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	@Override
	public String toString() {
		return "TrainSeat [id=" + id + ", trainId=" + trainId + ", seatNo=" + seatNo + "]";
	}

}
