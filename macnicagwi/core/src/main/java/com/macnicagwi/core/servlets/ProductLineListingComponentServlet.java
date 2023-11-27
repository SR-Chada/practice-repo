package com.macnicagwi.core.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

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
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macnicagwi.core.components.dtos.FacetSearchRequestDto;
import com.macnicagwi.core.components.dtos.FacetSearchResponseDto;
import com.macnicagwi.core.components.dtos.SelectedSearchFacetDto;
import com.macnicagwi.core.components.models.ProductLineListing;
import com.macnicagwi.core.components.models.impl.ProductLineListingImpl;
import com.macnicagwi.core.models.ProductLineCard;
import com.macnicagwi.core.services.MacnicaFacetSearchService;

/**
 * Macnica Product Line Listing Component Servlet. This servlet returns Search
 * Result as Child Product Lines Page Details.
 * 
 * @author Sumit
 */
//To-Do: Handle exception scenarios

@Component(name = "Macnica Product Line Listing Component Servlet", immediate = true, service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = { ProductLineListingImpl.RESOURCE_TYPE }, methods = {
		HttpConstants.METHOD_GET }, extensions = { ExporterConstants.SLING_MODEL_EXTENSION })
@SlingServletName(servletName = "Macnica Product Line Listing Component Servlet")

public class ProductLineListingComponentServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductLineListingComponentServlet.class);
	private static final String PAGE_TYPE_PROPERTY_PATH="jcr:content/pageType";
	private static final String PRODUCT_LINE_PAGE_TYPE="productLineLandingPage";

	@Reference
	private transient MacnicaFacetSearchService macnicaFacetSearchService;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			ProductLineListing productLineListingComponent = request.adaptTo(ProductLineListing.class);
			String searchRootPath = productLineListingComponent.getSearchPageRootPath();

			SelectedSearchFacetDto selectedFacets = new SelectedSearchFacetDto(PAGE_TYPE_PROPERTY_PATH, Collections.singletonList(PRODUCT_LINE_PAGE_TYPE ));
			FacetSearchRequestDto facetSearchRequestDto = new FacetSearchRequestDto();
			facetSearchRequestDto.setSelectedSearchFacets(Collections.singletonList(selectedFacets));
			facetSearchRequestDto.setSearchPageRootPath(searchRootPath);

			List<?> searchResults = new ArrayList<>();
			List<Page> pageResults = macnicaFacetSearchService.getPageResults(facetSearchRequestDto);
			int searchResultsTotalCount = macnicaFacetSearchService.getPageResultsTotalCount(facetSearchRequestDto);
			if (!pageResults.isEmpty()) {
				searchResults = pageResults.stream().map(page -> page.adaptTo(ProductLineCard.class))
						.filter(Objects::nonNull).collect(Collectors.toList());
			}
			FacetSearchResponseDto facetSearchResponse = new FacetSearchResponseDto(searchResultsTotalCount, Collections.emptyList(),
					searchResults);

			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(Constants.CT_JSON);
			new ObjectMapper().writeValue(response.getWriter(), facetSearchResponse);
		} catch (Exception ex) {
			LOGGER.error("Exception in Macnica Product Line Listing Component Servlet: ", ex);
		}
	}
}
