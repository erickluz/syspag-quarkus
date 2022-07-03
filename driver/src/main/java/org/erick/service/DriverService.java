package org.erick.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.erick.domain.Driver;
import org.erick.domain.DriverAvailability;
import org.erick.repository.DriverRepository;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@ApplicationScoped
public class DriverService {
    private static final Logger LOG = Logger.getLogger(DriverService.class);
    private final static String QUEUE_NAME = "driverTrip";
	@ConfigProperty(name = "driver.rabbitmq.host")
	private String rabbitMQhost;
	@ConfigProperty(name = "driver.rabbitmq.username")
	private String rabbitMQUser;
	@ConfigProperty(name = "driver.rabbitmq.password")
	private String rabbitMQPassword;
    @Inject
    private DriverRepository driverRepository;
    
    @Transactional
    public Driver save(Driver Driver) {
        return driverRepository.getEntityManager().merge(Driver);
    }

    public List<Driver> listAll() {
        return driverRepository.listAll();
    }

    public Driver findById(Long id) {
        return driverRepository.findById(id);
    }

    public void signalAvailability(DriverAvailability driverAvailability) {
        sendMessage(toJson(driverAvailability));
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
