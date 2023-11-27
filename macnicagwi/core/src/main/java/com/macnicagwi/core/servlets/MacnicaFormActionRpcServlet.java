package com.macnicagwi.core.servlets;

import java.io.IOException;

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
import com.google.gson.JsonObject;
import com.macnicagwi.core.utils.MacnicaFormUtils;

import lombok.NonNull;

/**
 * This servlet is used by the macnica form container as a form action 
 * to send the form data to a remote endpoint.
 * 
 * @author Sumit Kumar Agarwal
 * 
 */
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=" + MacnicaFormActionRpcServlet.RESOURCE_TYPE,
                "sling.servlet.selectors=" + MacnicaFormActionRpcServlet.SELECTOR
        }
)
public class MacnicaFormActionRpcServlet extends SlingAllMethodsServlet {

    static final String SELECTOR = "post";
    static final String RESOURCE_TYPE = "macnicagwi/components/content/form/actions/rpc";
    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaFormActionRpcServlet.class);
    private static final String PN_FORM_RPC_ENDPOINT_URL = "rpc_externalServiceEndPointUrl";

    @Override
    protected void doPost(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response)
            throws ServletException {
        
        try {
            Resource formContainerResource = request.getResource();
            ValueMap valueMap = formContainerResource.adaptTo(ValueMap.class);
            if (valueMap != null) {
                String endPointUrl = valueMap.get(PN_FORM_RPC_ENDPOINT_URL, String.class);
                
                JsonObject formData = MacnicaFormUtils.getJsonOfRequestParameters(request);

                formData.addProperty(PN_FORM_RPC_ENDPOINT_URL, endPointUrl);

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), formData.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Error in Macnica Form Action Rpc Servlet", e);
        }
    }
}
