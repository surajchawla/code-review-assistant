package com.codereview.webhook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubAppConfig {

    @Value("${github.webhook.secret}")
    private String webhookSecret;

    public String getWebhookSecret() {
        return webhookSecret;
    }
}