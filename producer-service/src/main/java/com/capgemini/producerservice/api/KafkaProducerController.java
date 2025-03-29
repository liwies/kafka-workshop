package com.capgemini.producerservice.api;


import com.capgemini.producerservice.service.KafkaProducerService;
import com.capgemini.producerservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public KafkaProducerController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/user-events")
    public User createUser(@RequestBody User user, @RequestParam("eventType") String eventType) {

        kafkaProducerService.sendMessage(user, eventType);
        System.out.println("Received user: " + user);
        System.out.println("Received event type: " + eventType);
        return user; // Returns the user
    }
}
