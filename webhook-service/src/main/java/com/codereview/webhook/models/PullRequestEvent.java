package com.codereview.webhook.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class PullRequestEvent {
    private String repo;
    private String owner;
    private int prNumber;
    private String action;
    private String branch;
    private String commitSha;
    private String sender;
}