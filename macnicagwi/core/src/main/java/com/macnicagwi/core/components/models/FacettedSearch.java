package com.macnicagwi.core.components.models;

import java.util.List;

import com.adobe.cq.wcm.core.components.models.Component;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * {@code FacettedSearch} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/facettedsearch} component.
 * @author Sumit
 */
@ConsumerType
public interface FacettedSearch extends Component {
    String getSearchPageRootPath();
    String getSearchAssetRootPath();
    String getFilterType();
    List<String> getFilterRootPaths();
    boolean isFreeTextSearch();
    String getFreeTextSearchPlaceholder();
    String getApplyButtonText();
    String getClearButtonText();
    String getLoadMoreButtonText();
    int getSearchResultsBlockSize();
    String getFilterButtonText();
    String getNoResultsFoundText();
    String getResourcePath();
}
