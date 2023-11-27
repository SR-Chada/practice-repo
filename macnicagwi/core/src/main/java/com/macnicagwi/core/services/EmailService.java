package com.macnicagwi.core.services;
import javax.jcr.Value;
import java.util.List;

public interface EmailService {
    void sendEmail(
            List<Value> toEmail,
            List<Value> ccEmail,
            String fromEmail,
            String subject,
            String content
    );
}