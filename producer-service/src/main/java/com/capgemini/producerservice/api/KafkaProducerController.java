package com.capgemini.producerservice.api;


import com.capgemini.producerservice.service.KafkaProducerService;
import com.capgemini.producerservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerController.class);
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public KafkaProducerController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/user-events")
    public User createUserEvent(@RequestBody User user, @RequestParam("eventType") String eventType) {
        kafkaProducerService.sendMessage(user, eventType);
        LOGGER.info("Received user {} ", user);
        LOGGER.info("Received event type {}", eventType);
        return user;
    }
}