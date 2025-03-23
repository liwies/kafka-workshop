package com.capgemini.consumerservice.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;

    public KafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "user-events", groupId = "user-group")
    public void consume(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String userId = jsonNode.get("user_id").asText();
            String eventType = jsonNode.get("event_type").asText();
            long timestamp = jsonNode.get("timestamp").asLong();
            System.out.println("Consumed message: User ID: " + userId + ", Event Type: " + eventType + ", Timestamp: " + timestamp);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception properly.  Consider dead-letter queues.
        }
    }
}