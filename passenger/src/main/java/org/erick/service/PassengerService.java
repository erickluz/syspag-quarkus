package org.erick.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.erick.domain.Passenger;
import org.erick.domain.TripRequest;
import org.erick.repository.PassengerRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@ApplicationScoped
public class PassengerService {
    private final static String QUEUE_NAME = "passengerTrip";

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
        factory.setHost("localhost");
        factory.setUsername("adm");
        factory.setPassword("adm");
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("message sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toJson(Object object) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}