package com.codereview.orchestrator.consumer;

import com.codereview.orchestrator.model.PullRequestEvent;
import com.codereview.orchestrator.service.OrchestratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PullRequestEventConsumer {

    private final ObjectMapper objectMapper;
    private final OrchestratorService orchestratorService;

    @KafkaListener(
            topics = "${PR_EVENTS_TOPIC}",
            groupId = "review-orchestrator"
    )
    public void handlePullRequestEvent(String message) {
        try {
            PullRequestEvent event = objectMapper.readValue(message, PullRequestEvent.class);
            log.info("Received PR event {}", event);
            orchestratorService.process(event);
        } catch (Exception e) {
            log.error("Failed to deserialize PullRequestEvent", e);
        }
    }
}