package com.macnicagwi.globalportal.core.models.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.SimpleSearch;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.globalportal.core.models.GlobalPortalLinkHandler;
import com.macnicagwi.globalportal.core.models.PageListItem;
import com.macnicagwi.globalportal.core.models.SearchResults;
import com.macnicagwi.globalportal.core.services.JcrSearchService;


@Model(adaptables = SlingHttpServletRequest.class, adapters = { SearchResults.class,
        ComponentExporter.class }, resourceType = SearchResultsImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class SearchResultsImpl extends AbstractComponentImpl implements SearchResults {

	
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsImpl.class);

    protected static final String RESOURCE_TYPE = "globalportal/components/searchresults";
    
    /**
     * Search root path
     */
    private static final String PN_SEARCH_IN = "searchIn";
    
    private static final String FACET_QUERY_STRING = "search";
    
    private static final String FACET_PAGENATION_FROM = "f";
    
    private static final String DEFAULT_BUTTON_LABEL = "Load More";
    
    private static final String DEFAULT_SEARCH_ROOT = "/content/macnicagwi/global/en";
    
    private static final String DEFAULT_PAGENATION_FROM = "0";
    
    private static final int DEFAULT_RESULTS_LIMIT = 10;

	private static final String ORDER_BY_PATH = "@jcr:content/path";

    @Self
    private GlobalPortalLinkHandler linkHandler;   

    @OSGiService
    @Required
    private QueryBuilder queryBuilder;

    @ScriptVariable
    private Page currentPage;  
   
    @ValueMapValue
    private Integer limit;
    
    @ValueMapValue
    private String buttonLabel;
    
    @ValueMapValue
    private String noResultsMessage;
    
    @ValueMapValue
    private String noKeywordMessage;
    
    private List<PageListItem> results;   

    
    private Integer totalResultsSize;
    
   
    private String queryString;

    @OSGiService
    private JcrSearchService searchService;


    @OSGiService
    private MimeTypeService mimeTypeService;

    /**
     * Path to the current card list component resource
     */

    private String resourcePath;
    
    /**
     * The resource resolver object
     */
    @SlingObject
    private ResourceResolver resourceResolver;
    
	@Override
	public List<PageListItem> getResults() {
		return results;
	}
	
	@Override
	public String getButtonLabel() {
		return Optional.ofNullable(buttonLabel).orElse(DEFAULT_BUTTON_LABEL);
	}
	
	@Override
	public String getNoResultsMessage() {
		return  Optional.ofNullable(noResultsMessage).orElse(StringUtils.EMPTY);
	}
	
	@Override
	public String getNoKeywordMessage() {		
		return Optional.ofNullable(noKeywordMessage).orElse(StringUtils.EMPTY);
	}

	@Override
	public Integer getTotalResultsSize() {
		return totalResultsSize;
	}

	@Override
	public String getQueryString() {
		return queryString;
	}
	
	@Override
	public String getResourcePath() {
		return resourcePath;
	}
	
    @PostConstruct
    private void init() {
    	queryString = request.getParameter(FACET_QUERY_STRING);
    	results = getPages().filter(Objects::nonNull).map(page -> new PageListItemImpl(linkHandler, page, getId(), RESOURCE_TYPE,resourceResolver))
    	        .filter(Objects::nonNull).collect(Collectors.toList());
    	
    }
	
	private Stream<Page> getPages(){		
		int maxItems = Optional.ofNullable(this.limit).orElse(DEFAULT_RESULTS_LIMIT);
		String offset = Optional.ofNullable(request.getParameter(FACET_PAGENATION_FROM)).orElse(DEFAULT_PAGENATION_FROM);
		//update logic to get root page
        String searchRootPath = Optional.ofNullable(currentPage.getParent().getPath()).orElse(DEFAULT_SEARCH_ROOT);
        if (!StringUtils.isBlank(queryString)) {
        	Map<String,String> predicates = new HashMap<>();
        	 predicates.put("fulltext", queryString);
             predicates.put("type", "cq:Page");
             predicates.put("path", searchRootPath);
             predicates.put("orderby", ORDER_BY_PATH );
             predicates.put("p.offset", "" + offset);
             predicates.put("p.limit", "" + maxItems);            
             return getSearchResults(predicates)
                     .map(SearchResult::getResources)
                     .map(it -> (Iterable<Resource>) () -> it)
                     .map(it -> StreamSupport.stream(it.spliterator(), false))
                     .orElseGet(Stream::empty)
                     .filter(Objects::nonNull)
                     .map(currentPage.getPageManager()::getContainingPage)
                     .filter(Objects::nonNull);
             }
        
        return Stream.empty();
	}
	
	private Optional<SearchResult> getSearchResults(Map<String, String> predicates) {
		Session session = resource.getResourceResolver().adaptTo(Session.class);
		if (predicates.isEmpty() || session == null) {
			LOGGER.warn("Predicate Map Size {}, Session Object {}", predicates.size(), session);
			return Optional.empty();
		}
		try {
			Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
			SearchResult result = query.getResult();
			totalResultsSize = Math.toIntExact(result.getTotalMatches());
			return Optional.of(result);

		} catch (Exception e) {
			LOGGER.error("Error in global portal free text search", e);
		}
		return Optional.empty();
	}
	
    /**
     * Gets the search result, or empty if an exception occurs.
     *
     * @param search The search for which to get the results.
     * @return The search result, or empty if {@link RepositoryException} occurs.
     */
    private Optional<SearchResult> safeGetSearchResult( final SimpleSearch search) {
        try {
            return Optional.of(search.getResult());
        } catch (RepositoryException e) {
            LOGGER.error("Unable to retrieve search results for query.", e);
        }
        return Optional.empty();
    }

	
	

}
