package com.capgemini.consumerservice.api;


import com.capgemini.consumerservice.KafkaConsumerExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaConsumerController {

    private final KafkaConsumerExample kafkaConsumerService;

    @Autowired
    public KafkaConsumerController(KafkaConsumerExample kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @GetMapping("/get-user-events")
    public void getUserEvents() {
        kafkaConsumerService.consume();
    }
}
