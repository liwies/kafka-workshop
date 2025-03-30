package com.capgemini.consumerservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;

    public KafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "user-events")
    public void consume(ConsumerRecord<String, String> record) {
        LOGGER.info(String.format("Consumed message: %s", record.value()));
        LOGGER.info(String.format("Partition: %s", record.partition()));
        LOGGER.info(String.format("Offset: %s", record.offset()));
        LOGGER.info(String.format("Key: %s", record.key()));

        // Process the consumed message here.
        try {
            processMessage(record.value());
        } catch (Exception e) {
            LOGGER.error("Error processing message", e);
        }
    }

    private void processMessage(String message) {
        // Your message processing logic here.
        // Example:
        LOGGER.info("Processing message: " + message);
        // ...
    }

    //Example using a different deserializer.
//    @KafkaListener(topics = "json-topic", groupId = "json-group", containerFactory = "kafkaJsonListenerContainerFactory")
//    public void consumeJson(ConsumerRecord<String, Object> record) {
//        LOGGER.info(String.format("Consumed JSON message: %s", record.value()));
//        LOGGER.info(String.format("Partition: %s", record.partition()));
//        LOGGER.info(String.format("Offset: %s", record.offset()));
//        LOGGER.info(String.format("Key: %s", record.key()));
//        try{
//            processJsonMessage(record.value());
//        }catch(Exception e){
//            LOGGER.error("Error processing JSON message", e);
//        }
//    }

    private void processJsonMessage(Object message){
        LOGGER.info("Processing JSON message: " + message);
    }
}