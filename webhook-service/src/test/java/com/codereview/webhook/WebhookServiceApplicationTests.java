package com.codereview.webhook;

import com.codereview.webhook.controllers.WebhookController;
import com.codereview.webhook.services.IEventParser;
import com.codereview.webhook.services.IKafkaEventPublisher;
import com.codereview.webhook.services.ISignatureValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebhookServiceApplicationTests {

    @Autowired
    private WebhookController webhookController;
    @Autowired
    private ISignatureValidator signatureValidator;
    @Autowired
    private IEventParser eventParser;
    @Autowired
    private IKafkaEventPublisher kafkaEventPublisher;

    @Test
    void contextLoads() {
        // Default Spring Boot context load test
    }

    @Test
    void mainRunsWithoutException() {
        WebhookServiceApplication.main(new String[]{});
    }

    @Test
    void webhookControllerBeanExists() {
        assertNotNull(webhookController);
    }

    @Test
    void signatureValidatorBeanExists() {
        assertNotNull(signatureValidator);
    }

    @Test
    void eventParserBeanExists() {
        assertNotNull(eventParser);
    }

    @Test
    void kafkaEventPublisherBeanExists() {
        assertNotNull(kafkaEventPublisher);
    }
}