package com.ticketbooking.user.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbooking.user.constants.DatabaseConstant.UserRole;
import com.ticketbooking.user.constants.DatabaseConstant.UserStatus;
import com.ticketbooking.user.dto.CommonApiResponse;
import com.ticketbooking.user.dto.UserListResponseDto;
import com.ticketbooking.user.dto.UserLoginRequest;
import com.ticketbooking.user.dto.UserLoginResponse;
import com.ticketbooking.user.entity.User;
import com.ticketbooking.user.service.CustomUserDetailsService;
import com.ticketbooking.user.service.UserService;
import com.ticketbooking.user.utility.JwtUtil;

@Component
public class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	ObjectMapper objectMapper = new ObjectMapper();

	public ResponseEntity<CommonApiResponse> registerUser(@RequestBody User user) {

		CommonApiResponse response = new CommonApiResponse();

		if (user == null) {
			response.setResponseMessage("user is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userService.getUserByEmailAndRole(user.getEmailId(),
				user.getRole());

		if (existingUser != null) {
			response.setResponseMessage("User already register");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);
		user.setStatus(UserStatus.ACTIVE.value());

		existingUser = this.userService.registerUser(user);

		if (existingUser == null) {
			response.setResponseMessage("failed to register user");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("User registered Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserLoginResponse> login(UserLoginRequest loginRequest) {

		UserLoginResponse response = new UserLoginResponse();

		if (loginRequest == null) {
			response.setResponseMessage("Missing Input");
			response.setSuccess(true);

			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String jwtToken = null;
		User user = null;
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(), loginRequest.getPassword()));
		} catch (Exception ex) {
			response.setResponseMessage("Invalid email or password.");
			response.setSuccess(true);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmailId());

		user = userService.getUserByEmail(loginRequest.getEmailId());

		if (user.getStatus() != UserStatus.ACTIVE.value()) {
			response.setResponseMessage("Failed to login");
			response.setSuccess(true);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		for (GrantedAuthority grantedAuthory : userDetails.getAuthorities()) {
			if (grantedAuthory.getAuthority().equals(loginRequest.getRole())) {
				jwtToken = jwtUtil.generateToken(userDetails.getUsername());
			}
		}

		// user is authenticated
		if (jwtToken != null) {
			response.setUser(user);

			response.setResponseMessage("Logged in sucessful");
			response.setSuccess(true);
			response.setJwtToken(jwtToken);
			return new ResponseEntity(response, HttpStatus.OK);

		}

		else {

			response.setResponseMessage("Failed to login");
			response.setSuccess(true);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

	}

	public ResponseEntity<CommonApiResponse> registerAdmin(User user) {

		CommonApiResponse response = new CommonApiResponse();

		if (user == null) {
			response.setResponseMessage("user is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (user.getEmailId() == null || user.getPassword() == null) {
			response.setResponseMessage("missing input");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userService.getUserByEmailAndRole(user.getEmailId(),
				user.getRole());

		if (existingUser != null) {
			response.setResponseMessage("User already register with this Email");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User admin = new User();
		admin.setEmailId(user.getEmailId());
		admin.setPassword(passwordEncoder.encode(user.getPassword()));
		admin.setRole(UserRole.ADMIN.value());
		admin.setStatus(UserStatus.ACTIVE.value());
		existingUser = this.userService.registerUser(admin);

		if (existingUser == null) {
			response.setResponseMessage("failed to register admin");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Admin registered Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> deleteUser(int userId) {

		CommonApiResponse response = new CommonApiResponse();

		if (userId == 0) {
			response.setResponseMessage("User Id is 0");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userService.getUserById(userId);

		user.setStatus(UserStatus.NOT_ACTIVE.value());

		User deletedUser = this.userService.updateUser(user);

		if (deletedUser == null) {
			response.setResponseMessage("Failed to delete the user");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("User deleted Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> updateUser(User user) {

		CommonApiResponse response = new CommonApiResponse();

		if (user == null) {
			response.setResponseMessage("Missing input");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User userDetailToUpdate = new User();

		User existingUser = this.userService.getUserById(user.getId());

		if (existingUser == null) {
			response.setResponseMessage("User not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		userDetailToUpdate.setId(existingUser.getId());
		userDetailToUpdate.setPassword(existingUser.getPassword());
		userDetailToUpdate.setEmailId(existingUser.getEmailId());
		userDetailToUpdate.setRole(existingUser.getRole());
		userDetailToUpdate.setStatus(existingUser.getStatus());
		userDetailToUpdate.setAge(user.getAge());
		userDetailToUpdate.setCity(user.getCity());
		userDetailToUpdate.setContact(user.getContact());
		userDetailToUpdate.setFirstName(user.getFirstName());
		userDetailToUpdate.setGender(user.getGender());
		userDetailToUpdate.setLastName(user.getLastName());
		userDetailToUpdate.setPincode(user.getPincode());
		userDetailToUpdate.setStreet(user.getStreet());

		User updatedUser = this.userService.updateUser(userDetailToUpdate);

		if (updatedUser == null) {
			response.setResponseMessage("Failed to update the user");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("User updated Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserListResponseDto> getUserDetailsById(int userId) {

		UserListResponseDto response = new UserListResponseDto();

		if (userId == 0) {
			response.setResponseMessage("User Id is 0");
			response.setSuccess(true);

			return new ResponseEntity<UserListResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userService.getUserById(userId);

		if (user == null) {
			response.setResponseMessage("User not found");
			response.setSuccess(true);

			return new ResponseEntity<UserListResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.setUsers(Arrays.asList(user));
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<UserListResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserListResponseDto> getAllUsers(String role) {

		UserListResponseDto response = new UserListResponseDto();

		List<User> users = new ArrayList<>();
		users = this.userService.getUsersByRoleAndStatus(role, UserStatus.ACTIVE.value());

		response.setUsers(users);
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<UserListResponseDto>(response, HttpStatus.OK);
	}


}
