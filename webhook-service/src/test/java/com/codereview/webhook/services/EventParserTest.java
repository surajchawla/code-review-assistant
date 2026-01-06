package com.codereview.webhook.services;

import com.codereview.webhook.models.PullRequestEvent;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class EventParserTest {

    @Test
    void parsesValidPayload() throws Exception {
        String payload = Files.readString(Path.of("src/test/fixtures/sample_pr_event.json"));
        IEventParser parser = new EventParser();

        PullRequestEvent event = parser.parse(payload);

        assertEquals("opened", event.getAction());
        assertEquals("my-repo", event.getRepo());
    }

    @Test
    void throwsOnMissingFields() throws Exception {
        // Missing "repository" and "pull_request"
        String payload = """
        {
          "action": "opened",
          "number": 42,
          "sender": { "login": "suraj" }
        }
        """;
        IEventParser parser = new EventParser();
        assertThrows(Exception.class, () -> parser.parse(payload));
    }

    @Test
    void throwsOnNonPREvent() throws Exception {
        // No "pull_request" node
        String payload = """
        {
          "action": "opened",
          "number": 42,
          "repository": {
            "name": "my-repo",
            "owner": { "login": "suraj" }
          },
          "sender": { "login": "suraj" }
        }
        """;
        IEventParser parser = new EventParser();
        assertThrows(Exception.class, () -> parser.parse(payload));
    }

    @Test
    void parsesNullStringFields() throws Exception {
        // All nodes present but with nulls
        String payload = """
        {
          "action": null,
          "number": null,
          "pull_request": {
            "head": {
              "sha": null,
              "ref": null
            }
          },
          "repository": {
            "name": null,
            "owner": { "login": null }
          },
          "sender": { "login": null }
        }
        """;
        IEventParser parser = new EventParser();
        PullRequestEvent event = parser.parse(payload);

        assertNotNull(event);
        assertEquals("", event.getAction());
        assertEquals(0, event.getPrNumber());
        assertEquals("", event.getRepo());
        assertEquals("", event.getOwner());
        assertEquals("", event.getBranch());
        assertEquals("", event.getCommitSha());
        assertEquals("", event.getSender());
    }

    @Test
    void throwsOnWrongTypes() throws Exception {
        // "number" is a string instead of int
        String payload = """
        {
          "action": "opened",
          "number": "not-a-number",
          "pull_request": {
            "head": {
              "sha": "abc123",
              "ref": "feature/my-branch"
            }
          },
          "repository": {
            "name": "my-repo",
            "owner": { "login": "suraj" }
          },
          "sender": { "login": "suraj" }
        }
        """;
        IEventParser parser = new EventParser();
        assertThrows(Exception.class, () -> parser.parse(payload));
    }
}