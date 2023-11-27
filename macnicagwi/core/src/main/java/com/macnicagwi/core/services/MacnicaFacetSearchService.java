package com.macnicagwi.core.services;

import java.util.List;
import java.util.Map;

import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.core.components.dtos.FacetSearchRequestDto;
import com.macnicagwi.core.models.SearchFilter;

/**
 * Macnica Facet Search Service
 * <p>
 * This service provides response for Facet Search Requests.
 * @author Sumit Kumar Agarwal
 */
public interface MacnicaFacetSearchService {
    public List<SearchFilter> getSearchFilters(FacetSearchRequestDto facetSearchRequestDto);
    public List<Page> getPageResults(FacetSearchRequestDto facetSearchRequestDto);
    public int getPageResultsTotalCount(FacetSearchRequestDto facetSearchRequestDto);
    public List<Asset> getAssetResults(FacetSearchRequestDto facetSearchRequestDto);
    public int getAssetResultsTotalCount(FacetSearchRequestDto facetSearchRequestDto);
	/**
	 * Helper method to generate search predicates
	 * 
	 * @param rootPath    : Parent path in which the search should be executed
	 * @param tagFilters  : Comma separated string of tags
	 * @param queryString : Search keyword
	 * @return : predicate map to be passed to query builder
	 */
    public Map<String, String> getPredicates(String searchRoot, String tagFilters, String queryString, String offset, String limit);
}
