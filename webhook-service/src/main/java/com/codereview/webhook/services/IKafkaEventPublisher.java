package com.codereview.webhook.services;

import com.codereview.webhook.models.PullRequestEvent;

public interface IKafkaEventPublisher {
    void publish(PullRequestEvent event);
}