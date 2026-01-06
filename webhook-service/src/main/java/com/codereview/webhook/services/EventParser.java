package com.codereview.webhook.services;

import com.codereview.webhook.models.PullRequestEvent;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class EventParser implements IEventParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PullRequestEvent parse(String payload) throws Exception {
        JsonNode root = objectMapper.readTree(payload);

        String action = root.path("action").asString();
        int prNumber = root.path("number").asInt();

        JsonNode prNode = root.path("pull_request");
        JsonNode repoNode = root.path("repository");

        String repo = repoNode.path("name").asString();
        String owner = repoNode.path("owner").path("login").asString();
        String branch = prNode.path("head").path("ref").asString();
        String commitSha = prNode.path("head").path("sha").asString();
        String sender = root.path("sender").path("login").asString();

        return new PullRequestEvent(
                repo,
                owner,
                prNumber,
                action,
                branch,
                commitSha,
                sender
        );
    }
}