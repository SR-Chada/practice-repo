package com.macnicagwi.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletName;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.granite.rest.Constants;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macnicagwi.core.components.dtos.FacetSearchRequestDto;
import com.macnicagwi.core.components.dtos.FacetSearchResponseDto;
import com.macnicagwi.core.components.dtos.SelectedSearchFacetDto;
import com.macnicagwi.core.components.models.Search;
import com.macnicagwi.core.components.models.impl.SearchImpl;
import com.macnicagwi.core.constants.MacnicaCoreConstants;
import com.macnicagwi.core.models.AssetSearchResultCard;
import com.macnicagwi.core.models.PageSearchResultCard;
import com.macnicagwi.core.services.MacnicaFacetSearchService;

/**
 * Macnica Search Component Servlet
 * This servlet returns All Search Result Page & Asset Details depending on Input Search Text.
 * 
 * @author Sumit Agarwal
 */
//To-Do: Handle exception scenarios

@Component(
	name = "Macnica Search Component Servlet", 
	immediate = true, 
	service = { Servlet.class })
@SlingServletResourceTypes(
	resourceTypes = { SearchImpl.RESOURCE_TYPE }, 
	methods = { HttpConstants.METHOD_POST }, 
	extensions = {ExporterConstants.SLING_MODEL_EXTENSION})
@SlingServletName(
	servletName = "Macnica Search Component Servlet")

public class SearchComponentServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchComponentServlet.class);

	@Reference
    private transient MacnicaFacetSearchService macnicaFacetSearchService;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		try (InputStream facetSearchRequestStream = request.getInputStream();) {

			Search searchComp = request.adaptTo(Search.class);

			FacetSearchRequestDto facetSearchRequestDto = new FacetSearchRequestDto();
			facetSearchRequestDto.setSearchPageRootPath(searchComp.getSearchPageRootPath());
			facetSearchRequestDto.setSearchAssetRootPath(searchComp.getSearchAssetRootPath());
			facetSearchRequestDto.setSearchResultsBlockSize(searchComp.getSearchResultsBlockSize());
			
			if ( facetSearchRequestStream.available() != 0) {
				FacetSearchRequestDto facetSearchRequestStreamDto = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).readValue(facetSearchRequestStream, FacetSearchRequestDto.class);

				if (facetSearchRequestStreamDto.getSearchResultsOffSet() != 0) {
					facetSearchRequestDto.setSearchResultsOffSet(facetSearchRequestStreamDto.getSearchResultsOffSet());
				}

				if (StringUtils.isNotBlank(facetSearchRequestStreamDto.getSearchText())) {
					facetSearchRequestDto.setSearchText(facetSearchRequestStreamDto.getSearchText());
				}
			}
			
			List<Object> searchResults = new ArrayList<>();
			int searchResultsTotalCount = 0;

			List<Page> pageResults = macnicaFacetSearchService.getPageResults(facetSearchRequestDto);
			searchResultsTotalCount = macnicaFacetSearchService.getPageResultsTotalCount(facetSearchRequestDto);

			if(!pageResults.isEmpty()){
				List<Object> pageSearchResultCards = pageResults.stream().map(page -> page.adaptTo(PageSearchResultCard.class)).filter(Objects::nonNull).collect(Collectors.toList());
				searchResults.addAll(pageSearchResultCards);
			}

			List<SelectedSearchFacetDto> selectedSearchFacetsForAssets = new ArrayList<>();
			selectedSearchFacetsForAssets.add(new SelectedSearchFacetDto(MacnicaCoreConstants.ASSET_METADATA_DOWNLOADABLE_PROPERTY_PATH, Collections.singletonList("true")));
			facetSearchRequestDto.setSelectedSearchFacets(selectedSearchFacetsForAssets);
			List<Asset> assetResults = macnicaFacetSearchService.getAssetResults(facetSearchRequestDto);
			searchResultsTotalCount += macnicaFacetSearchService.getAssetResultsTotalCount(facetSearchRequestDto);

			if(!assetResults.isEmpty()){
				List<Object> assetSearchResultCards = assetResults.stream().map(asset -> asset.adaptTo(AssetSearchResultCard.class)).filter(Objects::nonNull).collect(Collectors.toList());
				searchResults.addAll(assetSearchResultCards);
			}

			FacetSearchResponseDto facetSearchResponse = new FacetSearchResponseDto(searchResultsTotalCount, Collections.emptyList(), searchResults);

			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(Constants.CT_JSON);
			new ObjectMapper().writeValue(response.getWriter(), facetSearchResponse);
		} catch (Exception ex) {
			LOGGER.error("Exception in Macnica Search Component Servlet: ", ex);
		}

	}

}
