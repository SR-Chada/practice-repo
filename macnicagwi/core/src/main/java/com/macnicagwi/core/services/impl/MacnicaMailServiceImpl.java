package com.macnicagwi.core.services.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.macnicagwi.core.services.MacnicaMailService;
import com.macnicagwi.core.utils.MacnicaCoreUtils; 

/**
 * Macnica Mail Service.
 * <p>
 * A Generic Email service that sends an email to a given list of recipients. 
 * @author Sumit Kumar Agarwal
 */
@Component(service = MacnicaMailService.class, immediate = true)
@ServiceDescription("Macnica Mail Service")
public class MacnicaMailServiceImpl implements MacnicaMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaMailServiceImpl.class);
    public static final String EMAIL_SUBJECT = "subject";
    public static final String EMAIL_TO_BUSINESS = "Mail-To-Business";
    public static final String EMAIL_CC_BUSINESS = "Mail-Cc-Business";
    public static final String EMAIL_TO_CUSTOMER = "Mail-To-Customer";
    
    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Activate
    protected void activate() {
        LOGGER.trace("Macnica Mail Service Activated!");
    }

    @Override
    public void sendEmail(String templatePath, final JsonObject formData, final Map<String, List<String>> emailRecipients) {

        if (emailRecipients == null || emailRecipients.size() <= 0) {
            throw new IllegalArgumentException("Invalid Recipients");
        }
            
        Map<String, String> emailParams = new Gson().fromJson(formData, LinkedHashMap.class);
        
        try {
            for (Entry<String, List<String>> emailRecipientsEntry :emailRecipients.entrySet()) {
                
                List<InternetAddress> internetAddressRecipients = new ArrayList<>();
                emailRecipientsEntry.getValue().forEach( recipient -> {
                    try {
                        internetAddressRecipients.add(new InternetAddress(recipient));
                    } catch (AddressException e) {
                        LOGGER.warn("Invalid Email Address {} Passed to sendEmail(). Skipping.", recipient);
                    }
                });

                String finalTemplatePath = "";
                if (emailRecipientsEntry.getKey().equalsIgnoreCase(EMAIL_TO_BUSINESS) || 
                    emailRecipientsEntry.getKey().equalsIgnoreCase(EMAIL_CC_BUSINESS)) {
                        finalTemplatePath = templatePath.concat("-business.html");
                } else if (emailRecipientsEntry.getKey().equalsIgnoreCase(EMAIL_TO_CUSTOMER)) {
                    finalTemplatePath = templatePath.concat("-customer.html");
                }
                
                final MailTemplate mailTemplate = this.getMailTemplate(finalTemplatePath);
                final Class<? extends Email> mailType = this.getMailType(finalTemplatePath);
                final MessageGateway<Email> messageGateway = messageGatewayService.getGateway(mailType);
                final Email email = getEmail(mailTemplate, mailType, emailParams);
                
                if (emailRecipientsEntry.getKey().equalsIgnoreCase(EMAIL_TO_BUSINESS) || 
                    emailRecipientsEntry.getKey().equalsIgnoreCase(EMAIL_TO_CUSTOMER)) {
                    email.setTo(internetAddressRecipients);
                } else if (emailRecipientsEntry.getKey().equalsIgnoreCase(EMAIL_CC_BUSINESS)) {
                    email.setCc(internetAddressRecipients);
                }

                messageGateway.send(email);
            }    
        } catch (Exception e) {
            LOGGER.error("Error During Sending Email Operation: ", e);
        }
        
    }

    private Email getEmail(final MailTemplate mailTemplate, final Class<? extends Email> mailType, final Map<String, String> params) throws EmailException, MessagingException, IOException {

        Email email = mailTemplate.getEmail(StrLookup.mapLookup(params), mailType);
        
        if (params.containsKey(EMAIL_SUBJECT)) {
            email.setSubject(params.get(EMAIL_SUBJECT));
        }

        return email;
    }

    private Class<? extends Email> getMailType(String templatePath) {
        return templatePath.endsWith(".html") ? HtmlEmail.class : SimpleEmail.class;
    }

    private MailTemplate getMailTemplate(String templatePath) throws IllegalArgumentException {
        ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resourceResolverFactory, MACNICA_SUB_SERVICE);
        Session session = serviceResolver.adaptTo(Session.class);
        MailTemplate mailTemplate = null;
        mailTemplate = MailTemplate.create(templatePath, session);

        if (mailTemplate == null) {
            throw new IllegalArgumentException("Mail Template Path [ " + templatePath + " ] Could not be Resolve to a Valid Template");
        }
        return mailTemplate;
    }

}