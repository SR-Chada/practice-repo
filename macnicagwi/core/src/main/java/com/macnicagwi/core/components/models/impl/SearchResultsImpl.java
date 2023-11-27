package com.macnicagwi.core.components.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.dam.api.Asset;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.MacnicaAssetListItem;
import com.macnicagwi.core.components.models.MacnicaPageListItem;
import com.macnicagwi.core.components.models.SearchResults;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.models.MacnicaPredicateHandler;
import com.macnicagwi.core.services.JcrSearchService;
import com.macnicagwi.core.services.MacnicaFacetSearchService;

import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { SearchResults.class,
        ComponentExporter.class }, resourceType = SearchResultsImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchResultsImpl extends AbstractComponentImpl implements SearchResults {
    private static final Logger logger = LoggerFactory.getLogger(SearchResultsImpl.class);

    protected static final String RESOURCE_TYPE = "macnicagwi/components/content/searchresults";

    @Self
    private MacnicaLinkHandler linkHandler;

    @Self
    private MacnicaPredicateHandler predicateHandler;

    @OSGiService
    @Required
    private QueryBuilder queryBuilder;

    @ScriptVariable
    private Page currentPage;

    @Getter
    private List<MacnicaPageListItem> pages;
    
    @Getter
    private List<MacnicaAssetListItem> assets;

    @Getter
    private List<ListItem> listItems;

    @Getter
    private Integer totalResultsSize;
    
    @Getter
    private String queryString;

    @OSGiService
    private JcrSearchService searchService;

    @OSGiService
    private MacnicaFacetSearchService macnicaFacetSearchService;

    @OSGiService
    private MimeTypeService mimeTypeService;

    /**
     * Path to the current card list component resource
     */
    @Getter
    private String resourcePath;

    @PostConstruct
    private void init() {
       
      
        resourcePath = request.getResource().getPath();
        logger.trace("Initializing Search Results component. Resource path {} ", resourcePath);
        Map<String, String> predicates = predicateHandler.getPredicates();
        listItems = getSearchResults(predicates);
        queryString = predicateHandler.getQueryString();
        
      
       
    }
    
    private List<ListItem> getSearchResults(Map<String, String> predicates){
        logger.trace("Generating search result items for predicates {} ", predicates.entrySet());
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        if (predicates.isEmpty() || searchService == null || session == null) {
            logger.warn("Predicate Map Size {}, Session Object {}, SearchService Object {}", predicates.size(), session, searchService);
            return Collections.emptyList();
        }
        
        List<ListItem> searchResultsList = new ArrayList<>();
        
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
        List<Resource> results = new ArrayList<>();
        SearchResult searchResult = query.getResult();
        Iterator<Resource> resources = searchResult.getResources();
        resources.forEachRemaining(results::add);
        totalResultsSize = Math.toIntExact(searchResult.getTotalMatches());
        
        
        logger.trace("found {} total matches", searchResult.getTotalMatches());
        pages = new ArrayList<>();
        assets = new ArrayList<>();
        results.forEach(resource ->{
            Page pageResource = resource.adaptTo(Page.class);
            if(pageResource!=null) {   
                MacnicaPageListItem pageItem= new MacnicaPageListItemImpl(linkHandler, pageResource, getId());
                pages.add(pageItem);
                searchResultsList.add(pageItem);            
                
            }
            Asset assetResource = resource.adaptTo(Asset.class);
            if(assetResource!=null) {
                MacnicaAssetListItem assetItem = new MacnicaAssetListItemImpl(linkHandler, mimeTypeService, assetResource, currentPage.getLanguage(), getId());
                assets.add(assetItem);
                searchResultsList.add(assetItem);      
                
            }
        });
        
        
        return searchResultsList;
        
    }



}
