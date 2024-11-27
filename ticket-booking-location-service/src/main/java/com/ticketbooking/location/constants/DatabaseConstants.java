package com.ticketbooking.location.constants;

public class DatabaseConstants {
	
	public static enum LocationStatus {
		ACTIVE(1),
		NOT_ACTIVE(0);
		
		private int status;

	    private LocationStatus(int status) {
	      this.status = status;
	    }

	    public int value() {
	      return this.status;
	    }
	     
	}

}
