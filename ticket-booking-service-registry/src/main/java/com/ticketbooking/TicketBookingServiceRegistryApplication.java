package com.ticketbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TicketBookingServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingServiceRegistryApplication.class, args);
	}

}
