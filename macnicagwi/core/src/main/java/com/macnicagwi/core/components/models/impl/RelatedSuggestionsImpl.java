package com.macnicagwi.core.components.models.impl;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.RelatedSuggestions;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.services.JcrSearchService;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PATH;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_CONTENT_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_SORT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_FULLTEXT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_ARTICLE_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_SORT_DESC;

/**
 * ProductListing model implementation.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = { RelatedSuggestions.class,
		ComponentExporter.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = RelatedSuggestionsImpl.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class RelatedSuggestionsImpl extends AbstractComponentImpl implements RelatedSuggestions {

	/**
	 * The resource type.
	 */
	protected static final String RESOURCE_TYPE = "macnicagwi/components/content/relatedsuggestions";

	/**
	 * The logger.
	 */

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Self
	@Via(type = ResourceSuperType.class)
	List list;
	
	@Self
	MacnicaLinkHandler linkHandler;
	
	@ScriptVariable
	private PageManager pageManager;

	@ValueMapValue
	@Nullable
	private String parentPage;

	@ValueMapValue
	@Nullable
	private String maxItems; 

	@ValueMapValue
	@Nullable
	private String query; 

	@ValueMapValue
	@Nullable
	private String sortOrder; 

	/**
	 * The current page.
	 */
	@ScriptVariable
	private Page currentPage;

	private java.util.List<ListItem> listItems;
	
	private Collection<ListItem> relatedSuggestionItems;
	
	@ValueMapValue
	@Nullable
	private String orderBy; 

	@ValueMapValue
	@Nullable
	private String listFrom;

	@OSGiService
	private JcrSearchService searchService;

	

	@PostConstruct
	protected void initModel() {
		String currentPagePath = currentPage.getPath();
		logger.debug("Initializing related suggestions component at page {} list size{} ", currentPagePath, list.getListItems().size());
		
		//remove current page from suggestions list
		listItems = list.getListItems().stream().filter(item->!item.getPath().equalsIgnoreCase(currentPagePath)).collect(Collectors.toList());
		
		//convert list items to Macnica PageList Item
		
		relatedSuggestionItems =listItems.stream().map(item -> new MacnicaPageListItemImpl(component,linkHandler, pageManager.getPage(item.getPath()), getId())).collect(Collectors.toList());
		
		try{
		
		if(!listFrom.equals("static")){
		Map<String,String> map = new HashMap<String,String>();		
			map.put(SEARCH_PATH, parentPage);
    		map.put(SEARCH_CONTENT_TYPE, "cq:Page");
			map.put(SEARCH_LIMIT, maxItems);
			
		if(listFrom.equals("children") && (orderBy.equals(PN_ARTICLE_DATE))){
			map.put(SEARCH_ORDER_BY, "@jcr:content/date");
			if(sortOrder.equals(SEARCH_ORDER_SORT_DESC)){
				map.put(SEARCH_ORDER_BY_SORT, sortOrder);
			}			
			relatedSuggestionItems = generateListItems(parentPage, map);
 		}
		
		if(listFrom.equals("search") && orderBy.equals(PN_ARTICLE_DATE)){
			map.put(SEARCH_ORDER_BY, "@jcr:content/date");
			map.put(SEARCH_FULLTEXT,query);
			if(sortOrder.equals(SEARCH_ORDER_SORT_DESC)){
				map.put(SEARCH_ORDER_BY_SORT, sortOrder);
			}
			relatedSuggestionItems = generateListItems(parentPage, map);
 		}
		
	 }
}catch(Exception e){
	logger.info("Exception {}",e);
}

	}


	private java.util.List<ListItem> generateListItems(@NotNull String parentPage, @Nullable Map<String, String> map) {
		logger.trace("Generating items for predicates {} ", map.entrySet());
		if (map.isEmpty()) {
			logger.warn("No search predicates supplied to generate  items. Returning empty list");
			return Collections.emptyList();
		}
		// JCR lookup to build items
		logger.trace("Executing query to fetch pagelist for builing items");
		Session session = resource.getResourceResolver().adaptTo(Session.class);
		if (searchService == null || session == null) {
			logger.error(
					"Unable to generate items as session or searchSerice object is not available. session : {} searchService {}",
					session, searchService);
			return Collections.emptyList();
		}
		
			java.util.List<Page> pages = searchService.getPages(map, session);
			
			if (pages != null) {
				 
				return pages.stream().filter(Objects::nonNull)
						.map(page -> new MacnicaPageListItemImpl(component, linkHandler, page, getId()))
						.collect(Collectors.toList());
						
			}	
			
			logger.warn("No search results found");
		

		return Collections.emptyList();


	}
	

	@Override
	public Collection<ListItem> getListItems() {
		return relatedSuggestionItems;
	}

	@Override
	public String getOrderBy(){
		return orderBy;
	}

	@Override
	public String getListFrom(){
		return listFrom;
	}

	@Override
	public String getMaxItems(){
		return maxItems;
	}

	@Override
	public String getQuery(){
		return query;
	}
	
	@Override
	public String getSortOrder(){
		return sortOrder;
	}

	public String getParentPage(){
		return parentPage;
	}

}
