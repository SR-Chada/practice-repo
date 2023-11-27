package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.AT_SYMBOL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.macnicagwi.core.components.dtos.FacetSearchRequestDto;
import com.macnicagwi.core.components.dtos.SelectedSearchFacetDto;
import com.macnicagwi.core.components.models.ManufacturerListing;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
    adaptables = SlingHttpServletRequest.class, 
    adapters = { ManufacturerListing.class, ComponentExporter.class }, 
    resourceType = ManufacturerListingImpl.RESOURCE_TYPE, 
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, 
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ManufacturerListingImpl extends FacettedSearchImpl implements ManufacturerListing {
    public static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerListingImpl.class);
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/manufacturerlisting";
    public static final String PAGE_TYPE_PROPERTY_PATH ="jcr:content/pageType";
    public static final String MANUFACTURER_PAGE_RANKING_PROPERTY_PATH ="jcr:content/productManufacturerRanking";
    private static final String MANUFACTURER_PAGE_TYPE ="productManufacturerLandingPage";

    @Self
    SlingHttpServletRequest request;

    private FacetSearchRequestDto facetSearchRequestDto;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        LOGGER.trace("Manufacturer Listing Component Model Initialized.");

        facetSearchRequestDto = new FacetSearchRequestDto();
        List<SelectedSearchFacetDto> selectedSearchFacets = new ArrayList<>();
        
        facetSearchRequestDto.setSearchPageRootPath(getSearchPageRootPath());
        SelectedSearchFacetDto selectedSearchFacet = new SelectedSearchFacetDto(PAGE_TYPE_PROPERTY_PATH, Collections.singletonList(MANUFACTURER_PAGE_TYPE)); 
        selectedSearchFacets.add(selectedSearchFacet);
        facetSearchRequestDto.setSelectedSearchFacets(selectedSearchFacets);
        facetSearchRequestDto.setSortByProperty(AT_SYMBOL + MANUFACTURER_PAGE_RANKING_PROPERTY_PATH);
        facetSearchRequestDto.setSearchResultsBlockSize(getSearchResultsBlockSize());
        facetSearchRequestDto.setFilterType(getFilterType());
        facetSearchRequestDto.setFilterRootPaths(getFilterRootPaths());
    }

    @Override
    public FacetSearchRequestDto getFacetSearchRequestDto() {
        return facetSearchRequestDto;
    }
    
}
