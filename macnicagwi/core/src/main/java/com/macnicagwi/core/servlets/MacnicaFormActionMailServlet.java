package com.macnicagwi.core.servlets;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.macnicagwi.core.utils.MacnicaFormUtils;

import lombok.NonNull;

/**
 * This servlet is used by the macnica form container as a form action
 * to send mail on success reponse from remote endpoint to the related parties.
 * 
 * @author Sumit Kumar Agarwal
 * 
 */
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=" + MacnicaFormActionMailServlet.RESOURCE_TYPE,
                "sling.servlet.selectors=" + MacnicaFormActionMailServlet.SELECTOR
        }
)
public class MacnicaFormActionMailServlet extends SlingAllMethodsServlet {

    static final String SELECTOR = "post";
    static final String RESOURCE_TYPE = "macnicagwi/components/content/form/actions/mail";
    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaFormActionMailServlet.class);
    private static final String PN_FORM_MAIL_SUBJECT = "mail_subject";
    private static final String PN_FORM_MAIL_FROM = "mail_from";
    private static final String PN_FORM_MAIL_TO_BUSINESS = "mail_mailToBusiness";
    private static final String PN_FORM_MAIL_TO = "mail_mailto";
    private static final String PN_FORM_MAIL_CC = "mail_cc";
    private static final String PN_FORM_MAIL_TO_CUSTOMER = "mail_mailToCustomer";
    private static final String SENDER_EMAIL_ADDRESS = "senderEmailAddress";
    private static final String SUBJECT = "subject";


    @Override
    protected void doPost(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response)
            throws ServletException {
        
        try {
            Resource formContainerResource = request.getResource();
            ValueMap valueMap = formContainerResource.adaptTo(ValueMap.class);
            if (valueMap != null) {
                String mailSubject = valueMap.get(PN_FORM_MAIL_SUBJECT, String.class);
                String mailFrom = valueMap.get(PN_FORM_MAIL_FROM, String.class);
                boolean mailToBusiness = valueMap.get(PN_FORM_MAIL_TO_BUSINESS, Boolean.class);
                String[] mailToArr = valueMap.get(PN_FORM_MAIL_TO, String[].class);
                String[] mailCcArr = valueMap.get(PN_FORM_MAIL_CC, String[].class);
                boolean mailToCustomer = valueMap.get(PN_FORM_MAIL_TO_CUSTOMER, Boolean.class);
                
                JsonObject formData = MacnicaFormUtils.getJsonOfRequestParameters(request);

                formData.addProperty(SUBJECT, mailSubject);
                formData.addProperty(SENDER_EMAIL_ADDRESS, mailFrom);
                formData.addProperty(PN_FORM_MAIL_TO_BUSINESS, mailToBusiness);
                JsonArray mailToJsonArray = new JsonArray();
                Stream.of(mailToArr).forEach(mailToJsonArray::add);
                formData.add(PN_FORM_MAIL_TO, mailToJsonArray);
                JsonArray mailCcJsonArray = new JsonArray();
                Stream.of(mailCcArr).forEach(mailCcJsonArray::add);
                formData.add(PN_FORM_MAIL_CC, mailCcJsonArray);
                formData.addProperty(PN_FORM_MAIL_TO_CUSTOMER, mailToCustomer);

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), formData.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Error in Macnica Form Action Mail Servlet", e);
        }
    }

}
