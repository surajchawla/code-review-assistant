package com.codereview.webhook.controllers;

import com.codereview.webhook.models.PullRequestEvent;
import com.codereview.webhook.services.IEventParser;
import com.codereview.webhook.services.IKafkaEventPublisher;
import com.codereview.webhook.services.ISignatureValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebhookController {

    private final ISignatureValidator signatureValidator;
    private final IEventParser eventParser;
    private final IKafkaEventPublisher kafkaEventPublisher;
    private final String webhookSecret;

    public WebhookController(
            ISignatureValidator signatureValidator,
            IEventParser eventParser,
            IKafkaEventPublisher kafkaEventPublisher,
            @Value("${github.webhook.secret}") String webhookSecret
    ) {
        this.signatureValidator = signatureValidator;
        this.eventParser = eventParser;
        this.kafkaEventPublisher = kafkaEventPublisher;
        this.webhookSecret = webhookSecret;
    }

    @PostMapping("/webhook/github")
    public ResponseEntity<Object> handleWebhook(
            @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload
    ) {
        if (signature == null || !signatureValidator.validate(payload, signature, webhookSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        PullRequestEvent event;
        try {
            event = eventParser.parse(payload);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }

        kafkaEventPublisher.publish(event);
        return ResponseEntity.ok("Webhook received and processed");
    }
}