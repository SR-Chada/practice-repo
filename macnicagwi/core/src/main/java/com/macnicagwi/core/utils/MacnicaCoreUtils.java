package com.macnicagwi.core.utils;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Macnica Core Utils} Macnica Core Utils
 * Collections of core utility methods like getting resource resolver, getting session etc.
 * @author Sumit
 */
public class MacnicaCoreUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaCoreUtils.class);

    private MacnicaCoreUtils() {
        // Disallow instantiation
    }

    /**
     * Method to get a Resource Resolver Object
     *
     * @param resourceResolverFactory ResourceResolverFactory Object
     * @param subService              Name of the SubService
     * @return ResourceResolver
     */
    public static ResourceResolver getResourceResolver(final ResourceResolverFactory resourceResolverFactory, final String subService) {
        ResourceResolver resourceResolver = null;
        if (null != resourceResolverFactory && null != subService) {
            try {
                final Map<String, Object> authInfo = new HashMap<>();
                authInfo.put(ResourceResolverFactory.SUBSERVICE, subService);
                resourceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo);
                LOGGER.debug("MacnicaCoreUtils : getResourceResolver() : Got Resource Resolver for Sub Service {} ",
                        subService);
            } catch (final LoginException loginException) {
                LOGGER.error("MacnicaCoreUtils : getResourceResolver() : Exception While Getting Resource Resolver for Sub Service {} ",
                        subService, loginException);
            }
        }
        return resourceResolver;
    }

    /**
     * Method to close close the JCR Session
     *
     * @param session JCR Session
     */
    public static void closeSession(Session session) {
        if (session != null) {
            session.logout();
        }
    }

    /**
     * Method to close the Resource Resolver
     *
     * @param resolver Resource Resolver
     */
    public static void closeResourceResolver(ResourceResolver resolver) {
        if (resolver != null && resolver.isLive()) {
            resolver.close();
        }
    }

}
