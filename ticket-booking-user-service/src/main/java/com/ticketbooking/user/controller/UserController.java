package com.ticketbooking.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketbooking.user.dto.CommonApiResponse;
import com.ticketbooking.user.dto.UserListResponseDto;
import com.ticketbooking.user.dto.UserLoginRequest;
import com.ticketbooking.user.dto.UserLoginResponse;
import com.ticketbooking.user.entity.User;
import com.ticketbooking.user.resource.UserResource;

@RestController
@RequestMapping("api/user/")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	@Autowired
	private UserResource userResource;
	

	@PostMapping("/register")
	public ResponseEntity<CommonApiResponse> registerUser(@RequestBody User user) {
		return userResource.registerUser(user);
	}
	
	@PostMapping("login")	
	public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
		return userResource.login(userLoginRequest);
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<UserListResponseDto> fetchUser(@RequestParam("userId") int userId) {
		return userResource.getUserDetailsById(userId);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<CommonApiResponse> deleteUser(@RequestParam("userId") int userId) {
		return userResource.deleteUser(userId);
	}
	
	// UserRegisterDto, we will set only email, password & role  from UI
	@PostMapping("/admin/register")
	public ResponseEntity<CommonApiResponse> registerAdmin(@RequestBody User user) {
		return userResource.registerAdmin(user);
	}
	
	@PutMapping("/update")
	public ResponseEntity<CommonApiResponse> updateUser(@RequestBody User user) {
		return userResource.updateUser(user);
	}
	
	@GetMapping("/fetch/all")
	public ResponseEntity<UserListResponseDto> fetchAllUsers(@RequestParam("role") String role) {
		return userResource.getAllUsers(role);
	}

}
