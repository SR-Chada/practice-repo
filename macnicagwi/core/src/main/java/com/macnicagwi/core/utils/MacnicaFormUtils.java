package com.macnicagwi.core.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * {@code Macnica Form Utils} Macnica Form Utils
 * Collections of Form utility methods like getting JSON Request, User Inputs etc.
 * @author Sumit
 */
public class MacnicaFormUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaFormUtils.class);
    private static final String ELEMENT_NAME_PROPERTY = "name";
    private static final String ELEMENT_TITLE_PROPERTY = "jcr:title";
    private static final String USER_INPUT_MAIL_CONTENT = "dynamicMailContent";

    private static final Set<String> INTERNAL_PARAMETER = ImmutableSet.of(
            ":formstart",
            "_charset_",
            ":redirect",
            ":cq_csrf_token"
    );

    private MacnicaFormUtils() {
        // Disallow instantiation
    }

   /**
     * Converts request parameters to a JSON object and filter AEM specific parameters out.
     *
     * @param request - the current {@link SlingHttpServletRequest}
     * @return JSON object of the request parameters
     */
    public static JsonObject getJsonOfRequestParameters(SlingHttpServletRequest request) {
        JsonObject jsonObj = new JsonObject();
        Map<String, String[]> formInputMap = request.getParameterMap();
        StringBuilder dynamicMailContent = new StringBuilder();

        Map<String,String> formElementsMap = new HashMap<>();
        Iterator<Resource> iterator = request.getResource().listChildren();
        while(iterator.hasNext()){
            Resource resource = iterator.next();
            if(resource != null){
                String elementName = resource.getValueMap().get(ELEMENT_NAME_PROPERTY, StringUtils.EMPTY);
                String elementTitle = resource.getValueMap().get(ELEMENT_TITLE_PROPERTY, StringUtils.EMPTY);
                if(StringUtils.isNotBlank(elementName)) {
                    formElementsMap.put(elementTitle, elementName);
                }
            }
        }
        
        for (Map.Entry<String, String[]> formInput : formInputMap.entrySet()) {
            if (!INTERNAL_PARAMETER.contains(formInput.getKey())) {
                String inputName = formInput.getKey();
                String inputValue = null;
                String[] v = formInput.getValue();
                if (v.length > 1) {
                    JsonArray data = new JsonArray();
                    Stream.of(v).forEach(data::add);
                    jsonObj.add(formInput.getKey(), data);
                    inputValue = data.getAsString();
                } else if (v.length == 1) {
                    jsonObj.addProperty(formInput.getKey(), v[0]);
                    inputValue = v[0];
                }
                if(StringUtils.isNotBlank(inputValue)){
                    for (Map.Entry<String, String> formElement: formElementsMap.entrySet()) {
                        if (formElement.getValue().equalsIgnoreCase(inputName)) {
                            dynamicMailContent.append("<tr><td>" + formElement.getKey()+ "</td><td>" + inputValue + "</td></tr>");
                        }
                    }
                }
            }
        }
        if(dynamicMailContent.length() > 0){
            jsonObj.addProperty(USER_INPUT_MAIL_CONTENT, dynamicMailContent.toString());
        }

        return jsonObj;
    }

    public static boolean validateRequest(SlingHttpServletRequest request, String cookieName, String cookieValue) {
        HttpSession session = request.getSession();
        if (session.getMaxInactiveInterval() == 0 && session.getAttribute(cookieName).toString().equalsIgnoreCase(cookieValue)) {
            return true;
        }
        return false;
    }

}
