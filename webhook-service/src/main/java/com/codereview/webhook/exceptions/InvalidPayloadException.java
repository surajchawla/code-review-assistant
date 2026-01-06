package com.codereview.webhook.exceptions;

public class InvalidPayloadException extends RuntimeException {
    public InvalidPayloadException(String message) {
        super(message);
    }
}