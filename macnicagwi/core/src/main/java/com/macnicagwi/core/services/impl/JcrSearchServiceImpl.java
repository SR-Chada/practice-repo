package com.macnicagwi.core.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import com.day.cq.dam.api.Asset;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.core.constants.MacnicaCoreConstants;
import com.macnicagwi.core.services.JcrSearchService;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jcr Search Service Impl
 * <p>
 * This service allows users to getResources for search related implementations.
 * @author Sumit Kumar Agarwal
 */
@Component(service = JcrSearchService.class, immediate = true)
@ServiceDescription("JCR Search Service")
public class JcrSearchServiceImpl implements JcrSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JcrSearchServiceImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Activate
    protected void activate() {
        LOGGER.trace("Jcr Search Service Activated!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resource> getResources(Map<String, String> predicates, Session session) {
    	if(StringUtils.isBlank(predicates.get(MacnicaCoreConstants.SEARCH_PATH))) {
    		return Collections.emptyList();
    	}
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
        LOGGER.trace("Executing query {}", query);
        return executeQuery(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Page> getPages(Map<String, String> predicates, Session session) {
        List<Resource> resources = getResources(predicates, session);
        return convertResources(resources, Page.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Asset> getAssets(Map<String, String> predicates, Session session) {
        List<Resource> resources = getResources(predicates, session);
        return convertResources(resources, Asset.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalCount(Map<String, String> predicates, Session session) {
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
        SearchResult searchResult = query.getResult();
        long totalMatches = 0;
        if (searchResult != null) {
            totalMatches = searchResult.getTotalMatches();
        }
        return Math.toIntExact(totalMatches);
    }

    private static List<Resource> executeQuery(Query query) {
    	LOGGER.trace("Executing Query {} ",query.getPredicates());
        List<Resource> results = new ArrayList<>();
        SearchResult searchResult = query.getResult();
        Iterator<Resource> resources = searchResult.getResources();
        resources.forEachRemaining(results::add);
        return results;
    }

    private static <T> List<T> convertResources(List<Resource> resources, Class<T> classType) {
        List<T> results = new ArrayList<>();
        resources.forEach(resource -> {
            T element = resource.adaptTo(classType);
            if (element != null) {
                results.add(element);
            }
        });
        return results;
    }

}