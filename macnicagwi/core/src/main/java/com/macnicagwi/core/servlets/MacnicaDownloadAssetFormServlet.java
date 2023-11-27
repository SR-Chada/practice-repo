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

import org.apache.commons.lang3.StringUtils;
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
 * @author Sumit Kumar Agarwal
 * 
 */
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=" + MacnicaDownloadAssetFormServlet.RESOURCE_TYPE,
                "sling.servlet.selectors=" + MacnicaDownloadAssetFormServlet.SELECTOR
        }
)
public class MacnicaDownloadAssetFormServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaDownloadAssetFormServlet.class);
    private static final String MAIL_SUBJECT = "downlaodAssetMailSubject";
    private static final String MAIL_TO_BUSINESS = "downlaodAssetMailToBusiness";
    private static final String MAIL_TO = "downlaodAssetMailBusinessMailTo";
    private static final String MAIL_CC = "downlaodAssetMailBusinessMailCc";
    private static final String MAIL_TO_CUSTOMER = "downlaodAssetMailToCustomer";
    private static final String CUSTOMER_EMAIL = "customerEmail";

    private static final String DOWNLOAD_ASSET_FORM_PATH_PATTERN = "^(/.*)/macnicagwi/([a-z_-]+)/([a-z_-]+)/([a-z_-]+)/(.*)$";
    private static final String DOWNLOAD_ASSET_MAIL_TEMPLATE_PATH = "/conf/macnicagwi/%s/%s/%s/email-templates/download-asset-email-template";

    public static final String SELECTOR = "post";
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/form/actions/downloadAsset";
    public static final String ASSET_DOWNLOAD_REQUEST_COOKIE_NAME = "downloadRestrictedAssets";
	public static final String ASSET_DOWNLOAD_REQUEST_COOKIE_VALUE = "authorized-user";

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
                    if (!mailTo.isEmpty()) {
                        emailRecipients.put(EMAIL_TO_BUSINESS, mailTo);
                    }
                    if (!mailCc.isEmpty()) {
                        emailRecipients.put(EMAIL_CC_BUSINESS, mailCc);
                    }
                }
                
                if (mailToCustomer && formData.has(CUSTOMER_EMAIL) && StringUtils.isNotBlank(formData.get(CUSTOMER_EMAIL).getAsString())) {
                    emailRecipients.put(EMAIL_TO_CUSTOMER, Collections.singletonList(formData.get(CUSTOMER_EMAIL).getAsString()));
                }

                Matcher matcher = Pattern.compile(DOWNLOAD_ASSET_FORM_PATH_PATTERN).matcher(formResourcePath);
                if (matcher.find()) {
                    String downloadAssetMailTemplatePath = String.format(DOWNLOAD_ASSET_MAIL_TEMPLATE_PATH, matcher.group(2), matcher.group(3), matcher.group(4));
                    macnicaMailService.sendEmail(downloadAssetMailTemplatePath, formData, emailRecipients);
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                request.getSession().setMaxInactiveInterval(0);
                request.getSession().setAttribute(ASSET_DOWNLOAD_REQUEST_COOKIE_NAME, ASSET_DOWNLOAD_REQUEST_COOKIE_VALUE);
                Cookie downloadCookie = new Cookie(ASSET_DOWNLOAD_REQUEST_COOKIE_NAME, ASSET_DOWNLOAD_REQUEST_COOKIE_VALUE);
                downloadCookie.setPath("/");
                response.addCookie(downloadCookie);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), formData.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Error in Macnica Download Asset Form Servlet", e);
        }
    }

}
