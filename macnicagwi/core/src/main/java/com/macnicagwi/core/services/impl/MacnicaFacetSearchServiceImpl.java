package com.macnicagwi.core.services.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.AT_SYMBOL;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.COMMA_SEPARATOR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.DOT_SEPARATOR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ALL_RESULTS;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_CONTENT_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_CONTENT_TYPE_ASSET;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_FULLTEXT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_OFFSET;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_PATH;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_SORT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_SORT_DESC;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PATH;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PROPERTIES_AND_OPERATION;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PROPERTY_VALUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.dtos.FacetSearchRequestDto;
import com.macnicagwi.core.components.dtos.SelectedSearchFacetDto;
import com.macnicagwi.core.components.models.impl.MultiLevelFilterImpl;
import com.macnicagwi.core.models.SearchFilter;
import com.macnicagwi.core.services.JcrSearchService;
import com.macnicagwi.core.services.MacnicaFacetSearchService;
import com.macnicagwi.core.utils.MacnicaCoreUtils;

import lombok.NonNull;

@Component(service = MacnicaFacetSearchService.class, immediate = true)
@ServiceDescription("Macnica Facet Search Service")
public class MacnicaFacetSearchServiceImpl implements MacnicaFacetSearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaFacetSearchServiceImpl.class);
	private static final String SEARCH_PROPERTY = "_property";
	private static final String SEARCH_VALUE = "_value";

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private JcrSearchService jcrSearchService;

	private Session session;

	private TagManager tagManager;

	@Activate
	protected void activate() {
		LOGGER.trace("Macnica Facet Search Service Impl Activated!");
		ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resourceResolverFactory, MACNICA_SUB_SERVICE);
		session = serviceResolver.adaptTo(Session.class);
		tagManager = serviceResolver.adaptTo(TagManager.class);
	}

	@Override
	public List<SearchFilter> getSearchFilters(FacetSearchRequestDto facetSearchRequestDto) {
        List<SearchFilter> searchFilters = new ArrayList<>();

        if(facetSearchRequestDto.isGetSearchFacets() && !facetSearchRequestDto.getFilterType().equalsIgnoreCase("none") && 
			!facetSearchRequestDto.getFilterRootPaths().isEmpty()) {
            for (String filterRootPath: facetSearchRequestDto.getFilterRootPaths()) {
                Tag filterRootTag = tagManager.resolve(filterRootPath);
                if (filterRootTag != null) {
                    searchFilters.add(filterRootTag.adaptTo(SearchFilter.class));
                }
            }
        }
		
		return searchFilters;
	}

	@Override
	public List<Page> getPageResults(FacetSearchRequestDto facetSearchRequestDto) {
		if (StringUtils.isNotBlank(facetSearchRequestDto.getSearchPageRootPath())) {
			Map<String, String> facetSearchQueryPredicates = getFacetSearchQueryPredicates(facetSearchRequestDto, facetSearchRequestDto.getSearchPageRootPath(), NameConstants.NT_PAGE);
			return jcrSearchService.getPages(facetSearchQueryPredicates, session);
		} 
		
		return Collections.emptyList();
	}

	@Override
	public int getPageResultsTotalCount(FacetSearchRequestDto facetSearchRequestDto) {
		if (StringUtils.isNotBlank(facetSearchRequestDto.getSearchPageRootPath())) {
			Map<String, String> facetSearchQueryPredicates = getFacetSearchQueryPredicates(facetSearchRequestDto, facetSearchRequestDto.getSearchPageRootPath(), NameConstants.NT_PAGE);
			return jcrSearchService.getTotalCount(facetSearchQueryPredicates, session);
		}

		return 0;
	}

	@Override
	public List<Asset> getAssetResults(FacetSearchRequestDto facetSearchRequestDto) {
		if (StringUtils.isNotBlank(facetSearchRequestDto.getSearchAssetRootPath())) {
			Map<String, String> facetSearchQueryPredicates = getFacetSearchQueryPredicates(facetSearchRequestDto, facetSearchRequestDto.getSearchAssetRootPath(), DamConstants.NT_DAM_ASSET);
			return jcrSearchService.getAssets(facetSearchQueryPredicates, session);
		} 
			
		return Collections.emptyList();
	}

	@Override
	public int getAssetResultsTotalCount(FacetSearchRequestDto facetSearchRequestDto) {
		if (StringUtils.isNotBlank(facetSearchRequestDto.getSearchAssetRootPath())) {
			Map<String, String> facetSearchQueryPredicates = getFacetSearchQueryPredicates(facetSearchRequestDto, facetSearchRequestDto.getSearchAssetRootPath(), DamConstants.NT_DAM_ASSET);
			return jcrSearchService.getTotalCount(facetSearchQueryPredicates, session);
		}
		
		return 0;
	}


	/**
	 * Method to build Facet Search Query Predicates Map
	 *
	 * @param facetSearchRequestDto FacetSearchRequestDto
	 * @return facetSearchQuerypredicates Map<String, String>
	 */
	private Map<String, String> getFacetSearchQueryPredicates(final FacetSearchRequestDto facetSearchRequestDto, String contentRootPath, String contentType) {
		final Map<String, String> facetSearchQuerypredicates = new LinkedHashMap<>();
		if (StringUtils.isNotBlank(facetSearchRequestDto.getSearchText())) {
			facetSearchQuerypredicates.put(SEARCH_FULLTEXT, facetSearchRequestDto.getSearchText());
		}
		
		facetSearchQuerypredicates.put(SEARCH_PATH, contentRootPath);
		facetSearchQuerypredicates.put(SEARCH_CONTENT_TYPE, contentType);

		if (facetSearchRequestDto.getSelectedSearchFacets() != null
				&& !facetSearchRequestDto.getSelectedSearchFacets().isEmpty()) {
			if (facetSearchRequestDto.getSelectedSearchFacets().size() > 1) {
				facetSearchQuerypredicates.put(SEARCH_PROPERTIES_AND_OPERATION, "true");
			}
			for (SelectedSearchFacetDto facetDto : facetSearchRequestDto.getSelectedSearchFacets()) {
				facetSearchQuerypredicates.put(
						String.format(SEARCH_PROPERTY,
								facetSearchRequestDto.getSelectedSearchFacets().indexOf(facetDto) + 1),
						AT_SYMBOL + facetDto.getFacetKey());
				for (String propertyValue : facetDto.getFacetValues()) {
					facetSearchQuerypredicates.put(String.format(SEARCH_PROPERTY_VALUE,
							facetSearchRequestDto.getSelectedSearchFacets().indexOf(facetDto) + 1,
							facetDto.getFacetValues().indexOf(propertyValue) + 1), propertyValue);
				}
			}
		}

		if (StringUtils.isNotBlank(facetSearchRequestDto.getSortByProperty())) {
			facetSearchQuerypredicates.put(SEARCH_ORDER_BY, facetSearchRequestDto.getSortByProperty());
			facetSearchQuerypredicates.put(SEARCH_ORDER_BY_SORT, SEARCH_ORDER_SORT_DESC);
		} else {
			facetSearchQuerypredicates.put(SEARCH_ORDER_BY, SEARCH_ORDER_BY_PATH);
		}
		
		facetSearchQuerypredicates.put(SEARCH_OFFSET, String.valueOf(facetSearchRequestDto.getSearchResultsOffSet()));

		if (facetSearchRequestDto.getSearchResultsBlockSize() == 0) {
			facetSearchQuerypredicates.put(SEARCH_LIMIT, SEARCH_ALL_RESULTS);
		} else {
			facetSearchQuerypredicates.put(SEARCH_LIMIT,
					String.valueOf(facetSearchRequestDto.getSearchResultsBlockSize()));
		}

		return facetSearchQuerypredicates;
	}
	
	/**
	 * Helper method to generate search predicates
	 * 
	 * @param rootPath    : Parent path in which the search should be executed
	 * @param tagFilters  : Comma separated string of tags
	 * @param queryString : Search keyword
	 * @return : predicate map to be passed to query builder
	 */
	@Override
	public Map<String, String> getPredicates(@NotNull String searchRoot, @Nullable String tagFilters,
			@Nullable String queryString, String offset, String size) {
		LOGGER.trace("Generating predicates for directory: {}, tagFilters {} and queryString {}", searchRoot, tagFilters,
				queryString);
		// return empty map if root directory is not supplied
		if (StringUtils.isBlank(searchRoot))
			return Collections.emptyMap();

		final Map<String, String> predicates = new LinkedHashMap<>();

		predicates.put(SEARCH_PATH, searchRoot);
		predicates.put(SEARCH_CONTENT_TYPE, SEARCH_CONTENT_TYPE_ASSET);

		if (StringUtils.isNotEmpty(queryString))
			predicates.put(SEARCH_FULLTEXT, queryString);

		if (!StringUtils.isBlank(tagFilters)) {
			String[] appliedTags = tagFilters.split(COMMA_SEPARATOR);
			if (!StringUtils.isAllBlank(appliedTags)) {
				Map<String, List<String>> tagValueMap;
				tagValueMap = Arrays.asList(appliedTags).stream().filter(StringUtils::isNotBlank)
						.collect(Collectors.groupingBy(MultiLevelFilterImpl::getPropertyName));

				predicates.put(SEARCH_PROPERTIES_AND_OPERATION, "true");
				tagValueMap = tagValueMap.entrySet().stream()
								.filter(Objects::nonNull)
								.filter(entry -> StringUtils.isNotBlank(entry.getKey()) && Objects.nonNull(entry.getValue()))
								.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
				int counter=0;
				for(Entry<String, List<String>> entry : tagValueMap.entrySet()) {
					addToPredicateMap(++counter, predicates, entry);
				}						
					
			}

		}
		predicates.put(SEARCH_OFFSET, offset);
		predicates.put(SEARCH_LIMIT, size);
		return predicates;
	}
	
	private static void addToPredicateMap(int counter, @NonNull Map<String, String> predicateMap,
			@NonNull Entry<String, List<String>> predicateEntry) {
		predicateMap.put(counter + SEARCH_PROPERTY, "@jcr:content/metadata/" + predicateEntry.getKey());
		predicateEntry.getValue().stream().filter(StringUtils::isNotBlank).forEach(value -> {
			int valueCounter = 1;
			predicateMap.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + valueCounter + SEARCH_VALUE, value);
			valueCounter++;
		});
	}

}
