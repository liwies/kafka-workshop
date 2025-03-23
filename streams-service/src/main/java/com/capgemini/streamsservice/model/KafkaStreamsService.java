package com.capgemini.streamsservice.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Service;

@Service
@EnableKafkaStreams
public class KafkaStreamsService {

    private final ObjectMapper objectMapper;

    public KafkaStreamsService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public KStream<String, String> processUserEvents(StreamsBuilder builder) {
        // 1.  Consume from the 'user-events' topic.
        KStream<String, String> userEventsStream = builder.stream("user-events");

        // 2.  Parse the JSON value and extract fields.
        KStream<String, String> parsedStream = userEventsStream.map((key, value) -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(value);
                String userId = jsonNode.get("user_id").asText();
                String eventType = jsonNode.get("event_type").asText();
                long timestamp = jsonNode.get("timestamp").asLong();
                String newValue = String.format("{\"user_id\":\"%s\",\"event_type\":\"%s\",\"timestamp\":%d}", userId, eventType, timestamp);
                return KeyValue.pair(userId, newValue); // Use userId as the key
            } catch (Exception e) {
                e.printStackTrace(); // Handle error
                return KeyValue.pair(key, "{\"error\":\"parse_error\"}"); // Return error
            }
        });

        // 3. Filter for 'login' events.
        KStream<String, String> loginEventsStream = parsedStream.filter((key, value) -> value.contains("\"event_type\":\"login\""));

        // 4.  Send the filtered login events to a new topic.
        loginEventsStream.to("login-events");

        // 5.  Print some events to the console (for demonstration).
        loginEventsStream.foreach((key, value) -> System.out.println("Kafka Streams - Login Event: Key=" + key + ", Value=" + value));

        return loginEventsStream; // Important: Return the stream.
    }
}
