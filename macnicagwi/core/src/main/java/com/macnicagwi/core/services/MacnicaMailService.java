package com.macnicagwi.core.services;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

/**
 * Macnica Mail Service.
 * <p>
 * A Generic Email service that sends an email to a given list of recipients. 
 * @author Sumit Kumar Agarwal
 */
public interface MacnicaMailService {

    void sendEmail(final String templatePath, final JsonObject formData, final Map<String, List<String>> emailRecipients);

}

