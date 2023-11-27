package com.macnicagwi.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.granite.rest.Constants;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macnicagwi.core.components.dtos.FacetSearchRequestDto;
import com.macnicagwi.core.components.dtos.FacetSearchResponseDto;
import com.macnicagwi.core.components.dtos.SelectedSearchFacetDto;
import com.macnicagwi.core.components.models.ManufacturerListing;
import com.macnicagwi.core.components.models.impl.ManufacturerListingImpl;
import com.macnicagwi.core.models.ManufacturerCard;
import com.macnicagwi.core.models.SearchFilter;
import com.macnicagwi.core.services.MacnicaFacetSearchService;
import com.macnicagwi.core.utils.MacnicaComponentUtils;

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

/**
 * Macnica Manufacturer Listing Component Servlet
 * This servlet returns Search Result Manufacturer Page Details depending on Input / Selected Search Filters.
 * This servlet returns All Possible Search Facets depending on Search Result Asset Details.
 * 
 * @author Sumit Agarwal
 */
//To-Do: Handle exception scenarios

@Component(
	name = "Macnica Manufacturer Listing Component Servlet", 
	immediate = true, 
	service = { Servlet.class })
@SlingServletResourceTypes(
	resourceTypes = { ManufacturerListingImpl.RESOURCE_TYPE }, 
	methods = { HttpConstants.METHOD_POST }, 
	extensions = {ExporterConstants.SLING_MODEL_EXTENSION})
@SlingServletName(
	servletName = "Macnica Manufacturer Listing Component Servlet")

public class ManufacturerListingComponentServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerListingComponentServlet.class);

	@Reference
    private transient MacnicaFacetSearchService macnicaFacetSearchService;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		try (InputStream facetSearchRequestStream = request.getInputStream();) {

			FacetSearchRequestDto facetSearchRequestDto = request.adaptTo(ManufacturerListing.class).getFacetSearchRequestDto();
			
			if ( facetSearchRequestStream.available() != 0) {
				FacetSearchRequestDto facetSearchRequestStreamDto = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).readValue(facetSearchRequestStream, FacetSearchRequestDto.class);

				if (facetSearchRequestStreamDto.getSearchResultsOffSet() != 0) {
					facetSearchRequestDto.setSearchResultsOffSet(facetSearchRequestStreamDto.getSearchResultsOffSet());
				}

				if (StringUtils.isNotBlank(facetSearchRequestStreamDto.getSearchText())) {
					facetSearchRequestDto.setSearchText(facetSearchRequestStreamDto.getSearchText());
				}

				if (facetSearchRequestStreamDto.getSelectedSearchFacets() != null && !facetSearchRequestStreamDto.getSelectedSearchFacets().isEmpty()) {
					List<SelectedSearchFacetDto> selectedSearchFacetsFromResource = facetSearchRequestDto.getSelectedSearchFacets();
					List<SelectedSearchFacetDto> selectedSearchFacetsFromRequest = facetSearchRequestStreamDto.getSelectedSearchFacets();
					selectedSearchFacetsFromRequest.addAll(selectedSearchFacetsFromResource);
					facetSearchRequestDto.setSelectedSearchFacets(MacnicaComponentUtils.getPageSpecificSelectedSearchFacets(selectedSearchFacetsFromRequest));
				}

				facetSearchRequestDto.setGetSearchFacets(facetSearchRequestStreamDto.isGetSearchFacets());
			}

			List<SearchFilter> searchFilters = macnicaFacetSearchService.getSearchFilters(facetSearchRequestDto);
			
			List<?> searchResults = new ArrayList<>();
			List<Page> pageResults = macnicaFacetSearchService.getPageResults(facetSearchRequestDto);
			int searchResultsTotalCount = macnicaFacetSearchService.getPageResultsTotalCount(facetSearchRequestDto);

			if(!pageResults.isEmpty()){
				searchResults= pageResults.stream().map(page -> page.adaptTo(ManufacturerCard.class)).filter(Objects::nonNull).collect(Collectors.toList());
			}

			FacetSearchResponseDto facetSearchResponse = new FacetSearchResponseDto(searchResultsTotalCount, searchFilters, searchResults);

			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(Constants.CT_JSON);
			new ObjectMapper().writeValue(response.getWriter(), facetSearchResponse);
		} catch (Exception ex) {
			LOGGER.error("Exception in Macnica Facet Search Servlet: ", ex);
		}

	}

}
