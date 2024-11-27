package com.ticketbooking.train.constants;

public class DatabaseConstant {
	
	public static enum TrainStatus {
		ACTIVE(1),
		NOT_ACTIVE(0);
		
		private int status;

	    private TrainStatus(int status) {
	      this.status = status;
	    }

	    public int value() {
	      return this.status;
	    }
	     
	}

}
