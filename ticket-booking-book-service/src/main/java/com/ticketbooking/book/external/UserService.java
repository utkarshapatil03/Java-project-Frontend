package com.ticketbooking.book.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ticketbooking.book.dto.UserListResponseDto;

@Component
@FeignClient(name = "ticket-booking-user-service")
public interface UserService {
	
	@GetMapping("api/user/fetch")
	UserListResponseDto fetchUserById(@RequestParam("userId") int  userId);

}
