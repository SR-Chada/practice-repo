package com.macnicagwi.core.components.models;

import com.macnicagwi.core.components.dtos.FacetSearchRequestDto;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * {@code ManufacturerListing} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/manufacturerlisting} component.
 * @author Sumit
 */
@ConsumerType
public interface ManufacturerListing extends FacettedSearch {
    FacetSearchRequestDto getFacetSearchRequestDto();
}
