package com.ticketbooking.user.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonApiResponse {
	
	private String responseMessage;
	
	private HttpStatus status;
	
	private boolean isSuccess;  

}
