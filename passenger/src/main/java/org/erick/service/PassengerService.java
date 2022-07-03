package org.erick.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.erick.domain.Passenger;
import org.erick.domain.TripRequest;
import org.erick.repository.PassengerRepository;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@ApplicationScoped
public class PassengerService {
    private static final Logger LOG = Logger.getLogger(PassengerService.class);
    private final static String QUEUE_NAME = "passengerTrip";
	@ConfigProperty(name = "passenger.rabbitmq.host")
	private String rabbitMQhost;
	@ConfigProperty(name = "passenger.rabbitmq.username")
	private String rabbitMQUser;
	@ConfigProperty(name = "passenger.rabbitmq.password")
	private String rabbitMQPassword;
    
    @Inject
    private PassengerRepository passengerRepository;

    @Transactional
    public Passenger save(Passenger passenger) {
        return passengerRepository.getEntityManager().merge(passenger);
    }

    public List<Passenger> listAll() {
        return passengerRepository.listAll();
    }

    public Passenger findById(Long id) {
        return passengerRepository.findById(id);
    }

    public void requestTrip(TripRequest tripRequest) {
        sendMessage(toJson(tripRequest));
    }

    private void sendMessage(String message) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQhost);
        factory.setUsername(rabbitMQUser);
        factory.setPassword(rabbitMQPassword);
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (Exception e) {
            LOG.error("Error to send message to queue. ", e);
        }
    }

    private String toJson(Object object) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("Error to convert object to json.", e);
        }
        return null;
    }

}