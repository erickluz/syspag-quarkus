package org.erick.service;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.erick.domain.driver.DriverAvailability;
import org.erick.domain.passenger.TripRequestPassenger;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
public class TripMessage {
	private static final Logger LOG = Logger.getLogger(TripMessage.class);
	private final static String QUEUE_PASSENGER_NAME = "passengerTrip";
	private final static String QUEUE_DRIVER_NAME = "driverTrip";
	private Channel channelPassenger;
	private Channel channelDriver;
	
	@ConfigProperty(name = "trip.rabbitmq.host")
	private String rabbitMQhost;
	@ConfigProperty(name = "trip.rabbitmq.username")
	private String rabbitMQUser;
	@ConfigProperty(name = "trip.rabbitmq.password")
	private String rabbitMQPassword;

	@Inject
	private TripService tripService;

	@PostConstruct
	public void init() {
		LOG.debug("Setting Passenger queue ...");
		setupQueue(QUEUE_PASSENGER_NAME, channelPassenger, processPassengerTripMessage());
		LOG.debug("Setting Driver queue ...");
		setupQueue(QUEUE_DRIVER_NAME, channelDriver, processDriverMessage());
	}
	
	private ConnectionFactory getConnectionQueueFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitMQhost);
		factory.setUsername(rabbitMQUser);
        factory.setPassword(rabbitMQPassword);
		return factory;
	}

	private void setupQueue(String queue, Channel channel, DeliverCallback deliverCallback) {
		try {
			Connection connection = getConnectionQueueFactory().newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queue, false, false, false, null);
			channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
		} catch (Exception e) {
			LOG.error("Error to setup queue. Queue: " + queue, e);
		}
	}

	private DeliverCallback processPassengerTripMessage() {
		return (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			LOG.debug(" [x] Received " + message);
			tripService.passengerRequestsTrip((TripRequestPassenger) fromJson(message, TripRequestPassenger.class));
		};
	}

	private DeliverCallback processDriverMessage() {
		return (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			LOG.debug(" [x] Received " + message);
			tripService.driverSignalsAvailability((DriverAvailability) fromJson(message, DriverAvailability.class));
		};
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