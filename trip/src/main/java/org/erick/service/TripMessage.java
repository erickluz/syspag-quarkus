package org.erick.service;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.erick.domain.DriverAvailability;
import org.erick.domain.TripRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
public class TripMessage {
	private final static String QUEUE_PASSENGER_NAME = "passengerTrip";
	private final static String QUEUE_DRIVER_NAME = "driverTrip";
	private Channel channelPassenger;
	private Channel channelDriver;
	
	@Inject
	private TripService tripService;

	@PostConstruct
	public void init() {
		setupQueuePassenger();
		setupQueueDriver();
	}

	private void setupQueuePassenger() {
		System.out.println("Reading passengers requests...");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("adm");
        factory.setPassword("adm");
		try {
			Connection connection = factory.newConnection();
			this.channelPassenger = connection.createChannel();
			this.channelPassenger.queueDeclare(QUEUE_PASSENGER_NAME, false, false, false, null);
			DeliverCallback deliverCallback = processPassengerTripMessage();
			this.channelPassenger.basicConsume(QUEUE_PASSENGER_NAME, true, deliverCallback, consumerTag -> { });
			System.out.println("Queue setup");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupQueueDriver() {
		System.out.println("Reading driver requests...");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("adm");
        factory.setPassword("adm");
		try {
			Connection connection = factory.newConnection();
			this.channelDriver = connection.createChannel();
			this.channelDriver.queueDeclare(QUEUE_DRIVER_NAME, false, false, false, null);
			DeliverCallback deliverCallback = processDriverMessage();
			this.channelDriver.basicConsume(QUEUE_DRIVER_NAME, true, deliverCallback, consumerTag -> { });
			System.out.println("Queue setup");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DeliverCallback processPassengerTripMessage() {
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message + "'");
			TripRequest tripRequest = (TripRequest) fromJson(message, TripRequest.class);
			System.out.println(tripRequest);
		};
		return deliverCallback;
	}

	private DeliverCallback processDriverMessage() {
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message + "'");
			DriverAvailability driverAvailability = (DriverAvailability) fromJson(message, DriverAvailability.class);
			tripService.driverSignalsAvailability(driverAvailability);
		};
		return deliverCallback;
	}

	private Object fromJson(String json, Class<?> classe) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(json, classe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}