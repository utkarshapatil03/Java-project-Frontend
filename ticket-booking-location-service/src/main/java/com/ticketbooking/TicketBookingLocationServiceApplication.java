package com.ticketbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TicketBookingLocationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingLocationServiceApplication.class, args);
	}

}
