package com.codereview.webhook.services;

import com.codereview.webhook.models.PullRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaEventPublisher implements IKafkaEventPublisher {

    private final KafkaTemplate<String, PullRequestEvent> kafkaTemplate;

    @Value("${kafka.topics.pr-events}")
    private String topic;

    public KafkaEventPublisher(KafkaTemplate<String, PullRequestEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(PullRequestEvent event) {
        log.info("Publishing pull request event {}", event);
        kafkaTemplate.send(topic, event.getRepo() + "#" + event.getPrNumber(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        // Add structured logging here
                        System.err.println("Failed to publish event: " + ex.getMessage());
                    }
                });
    }
}