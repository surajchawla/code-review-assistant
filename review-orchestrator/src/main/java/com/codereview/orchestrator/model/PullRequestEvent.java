package com.codereview.orchestrator.model;

import lombok.Data;

@Data
public class PullRequestEvent {
    private String repo;
    private String owner;
    private int prNumber;
    private String action;
    private String branch;
    private String commitSha;
    private String sender;

    public CodeFetchRequest toCodeFetchRequest() {
        CodeFetchRequest req = new CodeFetchRequest();
        req.setRepo(repo);
        req.setPrNumber(prNumber);
        return req;
    }
}