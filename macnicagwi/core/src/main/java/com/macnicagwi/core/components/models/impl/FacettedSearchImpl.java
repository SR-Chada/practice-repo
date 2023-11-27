package com.macnicagwi.core.components.models.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.core.components.models.FacettedSearch;


@Model(adaptables = { SlingHttpServletRequest.class }, adapters = { FacettedSearch.class,
    ComponentExporter.class }, resourceType = { FacettedSearchImpl.RESOURCE_TYPE }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class FacettedSearchImpl extends AbstractComponentImpl implements FacettedSearch {

    protected static final String RESOURCE_TYPE = "macnicagwi/components/content/facettedSearch";
	private static final Logger LOGGER = LoggerFactory.getLogger(FacettedSearchImpl.class);

    @SlingObject
    private ResourceResolver resourceResolver;

	@ValueMapValue
    protected String searchPageRootPath;

    @ValueMapValue
    protected String searchAssetRootPath;

    @ValueMapValue
	private String filterType;

	@ValueMapValue
	private String[] filterRootPaths;

    @ValueMapValue
	private boolean freeTextSearch;

    @ValueMapValue
	private String freeTextSearchPlaceholder;

    @ValueMapValue
	private String applyButtonText;

    @ValueMapValue
	private String clearButtonText;

	@ValueMapValue
	private String loadMoreButtonText;

    @ValueMapValue
	private String filterButtonText;

	@ValueMapValue
	private String noResultsFoundText;

	@ValueMapValue
	private int searchResultsBlockSize;

    @ScriptVariable
    private Page currentPage;

    protected String resourcePath;

    @PostConstruct
	public void init() {
		LOGGER.trace("Initialising Facetted Search Model");
        resourcePath = request.getResource().getPath();
	}

    @Override
    public String getSearchPageRootPath() {
        return searchPageRootPath;
    }

    @Override
    public String getSearchAssetRootPath() {
        return searchAssetRootPath;
    }

    @Override
    public String getFilterType() {
        return filterType;
    }

    @Override
    public List<String> getFilterRootPaths() {
        if (filterRootPaths != null && filterRootPaths.length > 0) {
            return Arrays.asList(filterRootPaths);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isFreeTextSearch() {
        return freeTextSearch;
    }

    @Override
    public String getFreeTextSearchPlaceholder() {
        return freeTextSearchPlaceholder;
    }

    @Override
    public String getApplyButtonText() {
        return applyButtonText;
    }

    @Override
    public String getClearButtonText() {
        return clearButtonText;
    }

    @Override
    public String getLoadMoreButtonText() {
        return loadMoreButtonText;
    }

    @Override
    public int getSearchResultsBlockSize() {
        return searchResultsBlockSize;
    }

    @Override
    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public String getFilterButtonText() {
        return filterButtonText;
    }

    @Override
    public String getNoResultsFoundText() {
        return noResultsFoundText;
    }
    
}
