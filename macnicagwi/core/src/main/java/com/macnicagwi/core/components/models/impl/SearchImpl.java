package com.macnicagwi.core.components.models.impl;


import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.Search;

@Model(
    adaptables = SlingHttpServletRequest.class, 
    adapters = { Search.class, ComponentExporter.class }, 
    resourceType = SearchImpl.RESOURCE_TYPE, 
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, 
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchImpl extends FacettedSearchImpl implements Search {
    public static final Logger LOGGER = LoggerFactory.getLogger(SearchImpl.class);
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/search";

    @Self
    SlingHttpServletRequest request;

    @ValueMapValue
	private String searchResultsPagePath;

    @ValueMapValue
	@Default(values = "Search")
	private String searchPlaceholder;

    @ValueMapValue
	@Nullable
	private String downloadFormFragmentPath;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        LOGGER.trace("Search Component Model Initialized.");
    }

    @Override
	public String getSearchResultsPagePath() {
		return searchResultsPagePath;
	}

    @Override
	public String getSearchPlaceholder() {
		return searchPlaceholder;
	}

    @Override
	public String getDownloadFormFragmentPath() {
		return downloadFormFragmentPath;
	}
    
}
