package com.capgemini.consumerservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}")
    public void consume(ConsumerRecord<String, String>consumerRecord) {
        LOGGER.info("Consumed message - Partition: {}, Offset: {}, Key: {}, Value: {}",
                consumerRecord.partition(),
                consumerRecord.offset(),
                consumerRecord.key(),
                consumerRecord.value());
        processMessage(consumerRecord.value());
    }

    private void processMessage(String message) {
        LOGGER.info("Processing message: {}", message);
    }
}