package com.ticketbooking.book.dto;

import java.util.List;

public class UserListResponseDto extends CommonApiResponse {

	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
