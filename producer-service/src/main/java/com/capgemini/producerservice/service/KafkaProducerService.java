package com.capgemini.producerservice.service;

import com.capgemini.producerservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "user-events";

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(User user, String eventType) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("user", objectMapper.writeValueAsString(user));
            event.put("event_type",eventType);
            event.put("timestamp", System.currentTimeMillis());
            String jsonMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, jsonMessage);
            System.out.println("Message sent: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception properly in a production environment.
        }
    }
}
