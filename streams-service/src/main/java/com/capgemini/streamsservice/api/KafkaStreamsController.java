package com.capgemini.streamsservice.api;


import com.capgemini.streamsservice.service.KafkaStreamsService;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaStreamsController {

    private final KafkaStreamsService kafkaStreamsService;

    @Autowired
    public KafkaStreamsController(KafkaStreamsService kafkaStreamsService) {
        this.kafkaStreamsService = kafkaStreamsService;
    }

    @GetMapping("/getStreams")
    public void getUserEvents() {
        kafkaStreamsService.processUserEvents(new StreamsBuilder());
    }
}
