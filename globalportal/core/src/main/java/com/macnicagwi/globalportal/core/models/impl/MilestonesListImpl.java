package com.macnicagwi.globalportal.core.models.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragmentList;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.macnicagwi.globalportal.core.models.MilestoneItem;
import com.macnicagwi.globalportal.core.models.MilestonesList;

@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, adapters = { MilestonesList.class,
        ComponentExporter.class }, resourceType = MilestonesListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class MilestonesListImpl implements MilestonesList {

    private static final Logger LOG = LoggerFactory.getLogger(MilestonesListImpl.class);
    
        
    @ValueMapValue(name = ContentFragmentList.PN_MODEL_PATH, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String modelPath;
    // points to the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/milestones";


	private static final String DEFAULT_GLOBAL_PORTAL_CF_PATH = "/content/dam/globalportal/public/en/content-fragments/";


	private static final String JSON_PN_ITEMS_MAP = "itemsMap";

    List<MilestoneItem> milestoneItemList = new ArrayList<>();
    
    Set<Integer> keyList = new LinkedHashSet<>();
    
    HashMap<Integer, List<MilestoneItem>> milestoneMap = new HashMap<Integer, List<MilestoneItem>>();

    @SlingObject
    private ResourceResolver resourceResolver;
    
        
    @Self
    @Via(type = ResourceSuperType.class)
    private ContentFragmentList milestonesList;
    
    @ValueMapValue(name = ContentFragmentList.PN_PARENT_PATH, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String parentPath;
    
    
    
    
    @PostConstruct
    public void init() {
    	
        if (StringUtils.isEmpty(parentPath)) {
            parentPath = DEFAULT_GLOBAL_PORTAL_CF_PATH;
        }

        if (StringUtils.isEmpty(modelPath)) {
            LOG.warn("Please provide a model path");
            return;
        }

        Session session = resourceResolver.adaptTo(Session.class);
        if (session == null) {
            LOG.warn("Session was null therefore no query was executed");
            return;
        }

        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        if (queryBuilder == null) {
            LOG.warn("Query builder was null therefore no query was executed");
            return;
        }

        //unable to generate results when type = dam:Asset is added. Need to be analysed further. 
        Map<String, String> queryParameterMap = new HashMap<>();
        queryParameterMap.put("path", parentPath);
        queryParameterMap.put("1_property", JcrConstants.JCR_CONTENT + "/data/cq:model");
        queryParameterMap.put("1_property.value", modelPath);
       
        PredicateGroup predicateGroup = PredicateGroup.create(queryParameterMap);
        Query query = queryBuilder.createQuery(predicateGroup, session);
        query.setHitsPerPage(0);
        SearchResult searchResult = query.getResult();

        
        LOG.info("Milestone query statement{}", searchResult.getQueryStatement());

        // Query builder has a leaking resource resolver, so the following work around is required.
        ResourceResolver leakingResourceResolver = null;
        
        try {
            // Iterate over the hits if you need special information
            Iterator<Resource> resourceIterator = searchResult.getResources();
            while (resourceIterator.hasNext()) {
                Resource resource = resourceIterator.next();
                if (leakingResourceResolver == null) {
                    // Get a reference to QB's leaking resource resolver
                    leakingResourceResolver = resource.getResourceResolver();
                }
                MilestoneItem item = new MilestoneItem(resource);
                milestoneItemList.add(item);
                generateKeys();
            }
        } catch(Exception e) {
        	LOG.error("Unable to create milestone item from Content fragment",e);
        }
        	finally {        
            if (leakingResourceResolver != null) {
                // Always close the leaking query builder resource resolver
                leakingResourceResolver.close();
            }
        }
    	
    }
    
    

 
    @Override
    public String getId() {
        return milestonesList.getId();
    }
    
    @Override
    @JsonProperty(JSON_PN_ITEMS_MAP)
    public Map<Integer, List<MilestoneItem>> getMilestoneMap() {   
        return milestoneMap;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
    
    /**
     * Groups milestone items with respective decade.
     */
    private void generateKeys() {
        List<Integer> keys = new ArrayList<>();
        for (MilestoneItem item : milestoneItemList) {
            keys.add(item.getMilestoneDecade());
        }
        keyList = new LinkedHashSet<Integer>(keys);
        for (int key : keyList) {
            List<MilestoneItem> itemList = new ArrayList<MilestoneItem>();
            for (MilestoneItem item : milestoneItemList) {
                if (key == item.getMilestoneDecade()) {
                    itemList.add(item);
                }
            }
            milestoneMap.put(key, itemList);
        }
    }

}