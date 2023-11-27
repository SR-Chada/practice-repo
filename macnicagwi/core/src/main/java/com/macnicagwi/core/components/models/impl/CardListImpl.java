package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.COMMA_SEPARATOR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.DOT_SEPARATOR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_CONTENT_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_FULLTEXT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_OFFSET;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PATH;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PROPERTIES_AND_OPERATION;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
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
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.CardList;
import com.macnicagwi.core.components.models.MacnicaPageListItem;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.models.MacnicaPredicateHandler;
import com.macnicagwi.core.services.JcrSearchService;
import com.macnicagwi.core.services.MacnicaFacetSearchService;


import lombok.NonNull;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { CardList.class,
		ComponentExporter.class }, resourceType = CardListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class CardListImpl extends AbstractComponentImpl implements CardList {

	private static final Logger logger = LoggerFactory.getLogger(CardListImpl.class);

	protected static final String RESOURCE_TYPE = "macnicagwi/components/content/cardlist";


	private static final String SEARCH_PROPERTY = "_property";
	private static final String SEARCH_VALUE = "_value";


	@Self
	private MacnicaLinkHandler linkHandler;
	
	@Self
	private MacnicaPredicateHandler predicateHandler;

	@OSGiService
	@Required
	private QueryBuilder queryBuilder;

	@ScriptVariable
	private Page currentPage;

	@ValueMapValue
	@Nullable
	private String rootPath;

	@ValueMapValue
	@Nullable
	private Boolean showDescription;

	@ValueMapValue
	@Nullable
	private Boolean showTitle;

	@ValueMapValue
	@Nullable
	private Boolean showFeaturedImage;

	@ValueMapValue
	@Nullable
	private Boolean showArticleDate;
	
	@ValueMapValue
	@Nullable
	private Boolean linkItems;		

	@ValueMapValue
	@Nullable
	private String eventLocationLabel; 	
	
	@ValueMapValue
	@Nullable
	private String eventStartDateLabel; 	
	
	@ValueMapValue
	@Nullable
	private String eventEndDateLabel; 	
	
	@ValueMapValue
	@Nullable
	private String eventStatusLabel; 
	
	@ValueMapValue
	@Nullable	
	private boolean isfreeTextSearchEnabled; 	
	
	@ValueMapValue
	@Nullable
	private String[] tags;

	private List<MacnicaPageListItem> listItems;
	
	private Integer totalResultsSize;

	@OSGiService
	private JcrSearchService searchService;
	
	@OSGiService
	private MacnicaFacetSearchService macnicaFacetSearchService;

	/**
	 * Path to the current card list component resource
	 */
	private String resourcePath;

	
	@ValueMapValue
	@Nullable
	private Boolean showManufacturerLogo;

	@PostConstruct
	private void init() {
		
		try {
			resourcePath = request.getResource().getPath();
			logger.trace("Initializing Card List component. Resource path {} ", resourcePath);

			if (StringUtils.isBlank(rootPath)) {
				logger.warn(
						"Root path for card list component is not configured. Assigning current page path as root path");
				rootPath = currentPage.getPath();
			}

			Map<String, String> predicates = predicateHandler.getPredicates();
			listItems = generateListItems(rootPath, predicates);

			

		} catch (Exception e) {
			logger.error("Unable to intialize card list component", e);
		}

	}

	/**
	 * Helper method to generate card list items based on rootPath and Facets
	 * 
	 * @param rootPath: Root page path for generating items list
	 * @param facetMap
	 */
	private List<MacnicaPageListItem> generateListItems(@NotNull String rootPath, @Nullable Map<String, String> predicates) {
		logger.trace("Generating card list items for predicates {} ", predicates.entrySet());
		if (predicates.isEmpty()) {
			logger.warn("No search predicates supplied to generate card lsit items. Returning empty list");
			return Collections.emptyList();
		}
		// JCR lookup to build card list items
		logger.trace("Executing query to fetch pagelist for builing card list items");
		Session session = resource.getResourceResolver().adaptTo(Session.class);
		if (searchService == null || session == null) {
			logger.error(
					"Unable to generate card list items as session or searchSerice object is not available. session : {} searchService {}",
					session, searchService);
			return Collections.emptyList();
		}
		
			List<Page> pages = searchService.getPages(predicates, session);
			predicates.remove(SEARCH_OFFSET);
			predicates.remove(SEARCH_LIMIT);
			this.totalResultsSize = this.searchService.getTotalCount(predicates, session);
			
			if (pages != null) {
				logger.trace("Retrieved {} results after JCR lookup", pages.size());
				return pages.stream().filter(Objects::nonNull)
						.map(page -> new MacnicaPageListItemImpl(linkHandler, page, getId()))
						.collect(Collectors.toList());
			}
			logger.warn("No search results found");
		

		return Collections.emptyList();

	}


	/**
	 * Helper method to generate search predicates
	 * @param rootPath : Parent path in which the search should be executed
	 * @param tagFilters : Comma separated string of tags
	 * @param queryString : Search keyword
	 * @return : predicate map to be passed to query builder
	 */
	private Map<String, String> getPredicates(@NotNull String rootPath, @Nullable String tagFilters, @Nullable String queryString) {
		//return empty map if there are no filters applied
		if(StringUtils.isBlank(tagFilters) && StringUtils.isBlank(queryString))
			return Collections.emptyMap();
		
		final Map<String, String> predicates = new LinkedHashMap<>();
		
		predicates.put(SEARCH_PATH, rootPath);
		predicates.put(SEARCH_CONTENT_TYPE, NameConstants.NT_PAGE);
		
		if (StringUtils.isNotEmpty(queryString))
			predicates.put(SEARCH_FULLTEXT, queryString);
		
		if (!StringUtils.isBlank(tagFilters)) {
			String[] appliedTags = tagFilters.split(COMMA_SEPARATOR);
			if (!StringUtils.isAllBlank(appliedTags)) {
				Map<String, List<String>> tagValueMap;
				tagValueMap = Arrays.asList(appliedTags).stream().filter(StringUtils::isNotBlank)
						.collect(Collectors.groupingBy(MultiLevelFilterImpl::getPropertyName));

				predicates.put(SEARCH_PROPERTIES_AND_OPERATION, "true");
				tagValueMap.entrySet().stream().filter(Objects::nonNull)
						.filter(entry -> StringUtils.isNotBlank(entry.getKey()) && Objects.nonNull(entry.getValue()))
						.forEach(entry -> {
							int counter = 0;
							addToPredicateMap(++counter, predicates, entry);
						});
			}

		} 
		return predicates;
	}

	private static void addToPredicateMap(int counter, @NonNull Map<String, String> predicateMap,
			@NonNull Entry<String, List<String>> predicateEntry) {
		predicateMap.put(counter + SEARCH_PROPERTY, "@jcr:content/"+predicateEntry.getKey());
		predicateEntry.getValue().stream().filter(StringUtils::isNotBlank).forEach(value -> {
			int valueCounter = 1;
			predicateMap.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + valueCounter + SEARCH_VALUE, value);
			valueCounter++;
		});
	}

	/**
	 * Get a stream of all children of the specified parent page not deeper than the
	 * specified max depth. This call is recursive and expects that the caller uses
	 * startLevel = parent.getDepth().
	 *
	 * @param depth  The number of levels under the root page to be included.
	 * @param parent The root page.
	 * @return Stream of all children of the specified parent that are not deeper
	 *         than the end level.
	 */
	private static Stream<Page> collectChildren(int depth, @NotNull final Page parent) {
		if (depth <= 0) {
			return Stream.empty();
		}
		Iterator<Page> childIterator = parent.listChildren();
		if (!childIterator.hasNext()) {
			return Stream.empty();
		}
		return StreamSupport.stream(((Iterable<Page>) () -> childIterator).spliterator(), false)
				.flatMap(child -> Stream.concat(Stream.of(child), collectChildren(depth - 1, child)));
	}
	
	@Override
	@NonNull
	public Integer getTotalResultsSize() {
		return totalResultsSize;
	}

	@Override
	@Nullable
	public String getResourcePath() {
		return resourcePath;
	}

	@Override
	public Collection<MacnicaPageListItem> getListItems() {
		return listItems;
	}

	@Override
	public final boolean isEmpty() {
		return getListItems().isEmpty() || Objects.isNull(listItems);
	}

	@Override
	public String getId() {
		Resource resource = this.request.getResource();
		return ComponentUtils.getId(resource, this.currentPage, this.componentContext);
	}

	@Override
	public ComponentData getData() {
		Resource resource = this.request.getResource();
		if (ComponentUtils.isDataLayerEnabled(resource)) {
			return DataLayerBuilder.forComponent().withId(this::getId).withType(() -> RESOURCE_TYPE).build();
		}
		return null;
	}

	@Override
	public Boolean showDescription() {
		return showDescription;
	}

	@Override
	public Boolean showTitle() {
		return showTitle;
	}

	@Override
	public Boolean showFeaturedImage() {
		return showFeaturedImage;
	}

	@Override
	public Boolean showArticleDate() {
		return showArticleDate;
	}

	@Override
	public Boolean linkItems() {
		return linkItems;
	}

	@Override
	public boolean isfreeTextSearchEnabled() {
		return isfreeTextSearchEnabled;
	}
	
	@Override
	public String getEventLocationLabel() {
		return eventLocationLabel;
	}
	
	@Override
	public String getEventStartDateLabel() {
		return eventStartDateLabel;
	}
	
	@Override
	public String getEventEndDateLabel() {
		return eventEndDateLabel;		
	}
	@Override
	public String getEventStatusLabel() {
		return eventStatusLabel;
	}

	@Override
	public Boolean showManufacturerLogo() {
	    return showManufacturerLogo;
	}


}
