package com.codereview.webhook.services;

import com.codereview.webhook.models.PullRequestEvent;

public interface IEventParser {
    PullRequestEvent parse(String payload) throws Exception;
}