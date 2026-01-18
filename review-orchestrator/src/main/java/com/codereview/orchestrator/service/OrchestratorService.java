package com.codereview.orchestrator.service;

import com.codereview.orchestrator.model.PullRequestEvent;
import com.codereview.orchestrator.workflow.WorkflowRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrchestratorService {

    private final WorkflowRouter workflowRouter;

    public void process(PullRequestEvent event) {
        log.info("Processing pull request event {}", event);
        workflowRouter.route(event);
    }
}