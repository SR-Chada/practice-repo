package com.macnicagwi.core.services.impl;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.macnicagwi.core.services.EmailService;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Value;
import java.util.List;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.EQUAL_SEPARATOR;
import static com.macnicagwi.core.services.impl.EmailServiceImpl.SERVICE_NAME;

@Component(
        service = EmailService.class,
        property = {
                Constants.SERVICE_ID + EQUAL_SEPARATOR + SERVICE_NAME
        }
)
public class EmailServiceImpl implements EmailService {

    protected static final String SERVICE_NAME = "Email Service";
    private static final String TAG = EmailServiceImpl.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Reference
    MessageGatewayService messageGatewayService;

    @Override
    public void sendEmail(List<Value> toEmailList, List<Value> ccEmailList, String fromEmail, String subject, String content) {
        try {
            LOGGER.trace("content = {}", content);
            // Setting up the email message
            Email email = new SimpleEmail();
            // Get the details to send email
            email.setSubject(subject);
            email.setMsg(content);
            for (Value toEmail : toEmailList) {
                email.addTo(toEmail.toString());
            }
            for (Value ccEmail : ccEmailList) {
                email.addCc(ccEmail.toString());
            }
            LOGGER.trace("messageGatewayService : {} and email = {}", messageGatewayService, email);
            // Inject the message gateway service and send email
            MessageGateway<Email> messageGateway = messageGatewayService.getGateway(Email.class);
            LOGGER.trace("messageGateway = {}", messageGateway);
            // Send the email
            messageGateway.send(email);
        } catch (Exception e) {
            LOGGER.error("{}: exception occurred: {}", TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}