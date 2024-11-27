package com.ticketbooking.train.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticketbooking.train.dto.CommonApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
		
	@ExceptionHandler(TrainNotFoundException.class)
	public ResponseEntity<CommonApiResponse> handleUserNotFoundException(TrainNotFoundException ex) {
		String responseMessage = ex.getMessage();
		
		CommonApiResponse apiResponse = CommonApiResponse.builder().responseMessage(responseMessage).isSuccess(false).status(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(TrainBookingAddTicketsFailedException.class)
	public ResponseEntity<CommonApiResponse> handleTrainBookingAddTicketsFailedException(TrainBookingAddTicketsFailedException ex) {
		String responseMessage = ex.getMessage();
		
		CommonApiResponse apiResponse = CommonApiResponse.builder().responseMessage(responseMessage).isSuccess(false).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

}
