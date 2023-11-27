package com.macnicagwi.core.services;

import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;

import javax.jcr.Session;
import java.util.List;
import java.util.Map;

/**
 * Jcr Search Service
 * <p>
 * This service allows users to getResources for search related implementations.
 * @author Sumit Kumar Agarwal
 */
public interface JcrSearchService {

    /**
     * Get a list of resources based on search predicates passed in
     *
     * @param predicates the search predicates
     * @param session    the session used to traverse the repository
     * @return a list of resources matching the predicates
     */
    List<Resource> getResources(Map<String, String> predicates, Session session);

    /**
     * Get a list of resources based on search predicates passed in
     *
     * @param predicates the search predicates
     * @param session    the session used to traverse the repository
     * @return a list of pages matching the predicates
     */
    List<Page> getPages(Map<String, String> predicates, Session session);

    /**
     * Get a list of assets based on search predicates passed in
     *
     * @param predicates the search predicates
     * @param session    the session used to traverse the repository
     * @return a list of assets matching the predicates
     */
    List<Asset> getAssets(Map<String, String> predicates, Session session);

    /**
     * Get the total number of matches found for the predicates passed in
     *
     * @param predicates the search predicates
     * @param session    the session used to traverse the repository
     * @return the total matches found for the predicates
     */
    int getTotalCount(Map<String, String> predicates, Session session);
}

