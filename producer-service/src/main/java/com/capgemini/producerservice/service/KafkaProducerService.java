package com.capgemini.producerservice.service;

import com.capgemini.producerservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.name}")
    private String topic;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

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
            kafkaTemplate.send(topic, jsonMessage);
            LOGGER.debug("Message sent: {}", jsonMessage);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error occurred during JSON mapping {}", e.getMessage());
        }
    }
}