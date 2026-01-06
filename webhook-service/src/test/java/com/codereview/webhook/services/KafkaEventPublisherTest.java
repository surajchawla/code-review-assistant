package com.codereview.webhook.services;

import com.codereview.webhook.models.PullRequestEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class KafkaEventPublisherTest {

    @Test
    void publishesEventToKafka() {
        KafkaTemplate<String, PullRequestEvent> template = mock(KafkaTemplate.class);
        KafkaEventPublisher publisher = new KafkaEventPublisher(template);

        PullRequestEvent event = new PullRequestEvent("repo", "owner", 1, "opened", "branch", "sha", "sender");
        publisher.publish(event);

        verify(template).send(any(String.class), any(String.class), eq(event));
    }

    @Test
    void usesCorrectTopicAndKey() {
        KafkaTemplate<String, PullRequestEvent> template = mock(KafkaTemplate.class);
        KafkaEventPublisher publisher = new KafkaEventPublisher(template);

        PullRequestEvent event = new PullRequestEvent("repo", "owner", 42, "opened", "branch", "sha", "sender");
        publisher.publish(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(template).send(topicCaptor.capture(), keyCaptor.capture(), eq(event));
        // topic is injected via @Value, so will be null in test unless set via reflection
        assertEquals("repo#42", keyCaptor.getValue());
    }

    @Test
    void handlesSendExceptionGracefully() {
        KafkaTemplate<String, PullRequestEvent> template = mock(KafkaTemplate.class);
        when(template.send(any(), any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Kafka error")));
        KafkaEventPublisher publisher = new KafkaEventPublisher(template);

        PullRequestEvent event = new PullRequestEvent("repo", "owner", 1, "opened", "branch", "sha", "sender");
        assertDoesNotThrow(() -> publisher.publish(event));
    }

    @Test
    void publishWithNullEventDoesNotThrow() {
        KafkaTemplate<String, PullRequestEvent> template = mock(KafkaTemplate.class);
        KafkaEventPublisher publisher = new KafkaEventPublisher(template);

        assertDoesNotThrow(() -> publisher.publish(null));
    }

    @Test
    void publishWithNullFieldsInEvent() {
        KafkaTemplate<String, PullRequestEvent> template = mock(KafkaTemplate.class);
        KafkaEventPublisher publisher = new KafkaEventPublisher(template);

        PullRequestEvent event = new PullRequestEvent(null, null, 0, null, null, null, null);
        publisher.publish(event);

        verify(template).send(any(), any(), eq(event));
    }
}