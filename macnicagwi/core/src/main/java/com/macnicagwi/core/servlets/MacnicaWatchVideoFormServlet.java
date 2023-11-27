package com.macnicagwi.core.servlets;

import static com.macnicagwi.core.services.impl.MacnicaMailServiceImpl.EMAIL_CC_BUSINESS;
import static com.macnicagwi.core.services.impl.MacnicaMailServiceImpl.EMAIL_SUBJECT;
import static com.macnicagwi.core.services.impl.MacnicaMailServiceImpl.EMAIL_TO_BUSINESS;
import static com.macnicagwi.core.services.impl.MacnicaMailServiceImpl.EMAIL_TO_CUSTOMER;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.macnicagwi.core.services.MacnicaMailService;
import com.macnicagwi.core.utils.MacnicaFormUtils;

import lombok.NonNull;

/**
 * This servlet is used by the macnica form container as a form action 
 * to send mail to the related parties with user inputs
 * to set valid user session & cookie.
 * 
 * Watch Restricted Video Form Servlet 
 * 
 * @author Sumit Kumar Agarwal
 * 
 */
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=" + MacnicaWatchVideoFormServlet.RESOURCE_TYPE,
                "sling.servlet.selectors=" + MacnicaWatchVideoFormServlet.SELECTOR
        }
)
public class MacnicaWatchVideoFormServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaWatchVideoFormServlet.class);
    private static final String MAIL_SUBJECT = "watchVideoMailSubject";
    private static final String MAIL_TO_BUSINESS = "watchVideoMailToBusiness";
    private static final String MAIL_TO = "watchVideoMailBusinessMailTo";
    private static final String MAIL_CC = "watchVideoMailBusinessMailCc";
    private static final String MAIL_TO_CUSTOMER = "watchVideoMailToCustomer";
    private static final String CUSTOMER_EMAIL = "customerEmail";

    private static final String WATCH_VIDEO_FORM_PATH_PATTERN = "^(/.*)/macnicagwi/([a-z_-]+)/([a-z_-]+)/([a-z_-]+)/(.*)$";
    private static final String WATCH_VIDEO_MAIL_TEMPLATE_PATH = "/conf/macnicagwi/%s/%s/%s/email-templates/watch-video-email-template";

    public static final String SELECTOR = "post";
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/form/actions/watchVideo";
    public static final String WATCH_VIDEO_REQUEST_COOKIE_NAME = "watchRestrictedVideos";
	public static final String WATCH_VIDEO_REQUEST_COOKIE_VALUE = "authorized-user";

    @Reference
	private transient MacnicaMailService macnicaMailService;

    @Override
    protected void doPost(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response)
            throws ServletException {
        
        try {
            Resource formContainerResource = request.getResource();
            ValueMap valueMap = formContainerResource.adaptTo(ValueMap.class);
            if (valueMap != null) {
                String formResourcePath = request.getResource().getPath();
                String mailSubject = valueMap.get(MAIL_SUBJECT, String.class);
                boolean mailToBusiness = valueMap.get(MAIL_TO_BUSINESS, Boolean.class);
                List<String> mailTo = Arrays.asList(valueMap.get(MAIL_TO, String[].class));
                List<String> mailCc = Arrays.asList(valueMap.get(MAIL_CC, String[].class));
                boolean mailToCustomer = valueMap.get(MAIL_TO_CUSTOMER, Boolean.class);
                
                JsonObject formData = MacnicaFormUtils.getJsonOfRequestParameters(request);
                formData.addProperty(EMAIL_SUBJECT, mailSubject);

                Map<String, List<String>> emailRecipients = new LinkedHashMap<>();
                if (mailToBusiness) {
                    emailRecipients.put(EMAIL_TO_BUSINESS, mailTo);
                    emailRecipients.put(EMAIL_CC_BUSINESS, mailCc);
                }
                if (mailToCustomer) {
                    emailRecipients.put(EMAIL_TO_CUSTOMER, Collections.singletonList(formData.get(CUSTOMER_EMAIL).getAsString()));
                }

                Matcher matcher = Pattern.compile(WATCH_VIDEO_FORM_PATH_PATTERN).matcher(formResourcePath);
                if (matcher.find()) {
                    String watchVideoMailTemplatePath = String.format(WATCH_VIDEO_MAIL_TEMPLATE_PATH, matcher.group(2), matcher.group(3), matcher.group(4));
                    macnicaMailService.sendEmail(watchVideoMailTemplatePath, formData, emailRecipients);
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                request.getSession().setMaxInactiveInterval(0);
                request.getSession().setAttribute(WATCH_VIDEO_REQUEST_COOKIE_NAME, WATCH_VIDEO_REQUEST_COOKIE_VALUE);
                response.addCookie(new Cookie(WATCH_VIDEO_REQUEST_COOKIE_NAME, WATCH_VIDEO_REQUEST_COOKIE_VALUE));
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), formData.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Error in Macnica Watch Video Form Servlet", e);
        }
    }

}
