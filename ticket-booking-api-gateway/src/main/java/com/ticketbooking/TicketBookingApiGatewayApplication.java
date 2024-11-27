package com.ticketbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TicketBookingApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingApiGatewayApplication.class, args);
	}

}
