package com.capgemini.streamsservice.service;

import com.capgemini.streamsservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsService {
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsService.class);

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Value("${spring.kafka.topic.filtered-topic}")
    private String filteredTopic;

    public KafkaStreamsService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public KStream<String, String> processUserEvents(StreamsBuilder builder) {
        // 1.  Consume from the 'user-events' topic. Deserialization of bytes is of type String
        KStream<String, String> userEventsStream = builder.stream(topic,  Consumed.with(Serdes.String(), Serdes.String()));

        // 2.  Parse the JSON value and extract fields.
        KStream<String, String> parsedStream = userEventsStream.map((key, value) -> {
            try {
                LOGGER.info("value: {}", value);
                JsonNode jsonNode = objectMapper.readTree(value);
                User user = objectMapper.readValue(jsonNode.get("user").asText(), User.class);
                String userId = user.id();
                String eventType = jsonNode.get("event_type").asText();
                long timestamp = jsonNode.get("timestamp").asLong();
                String newValue = String.format("{\"user_id\":\"%s\",\"event_type\":\"%s\",\"timestamp\":%d}", userId, eventType, timestamp);
                return KeyValue.pair(userId, newValue);
            } catch (JsonProcessingException e) {
                LOGGER.error("Error occurred during processing {}", e.getMessage());
                return KeyValue.pair(key, "{\"error\":\"parse_error\"}");
            }
        });

        // 3. Filter for 'login' events.
        KStream<String, String> loginEventsStream = parsedStream.filter((key, value) -> value.contains("\"event_type\":\"login\""));

        // 4.  Send the filtered login events to a new topic.
        loginEventsStream.to(filteredTopic,  Produced.with(Serdes.String(), Serdes.String()));

        // 5.  Print some events to the console (for demonstration).
        loginEventsStream.foreach((key, value) -> LOGGER.info("Kafka Streams - Login Event: Key={}, Value={}", key, value));

        return loginEventsStream; //Return the stream.
    }
}
