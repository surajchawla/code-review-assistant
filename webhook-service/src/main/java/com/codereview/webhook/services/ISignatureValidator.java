package com.codereview.webhook.services;

public interface ISignatureValidator {
    boolean validate(String payload, String signatureHeader, String secret);
}