package com.ticketbooking.user.dto;

import java.util.List;

import com.ticketbooking.user.entity.User;

public class UserListResponseDto extends CommonApiResponse {

	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
