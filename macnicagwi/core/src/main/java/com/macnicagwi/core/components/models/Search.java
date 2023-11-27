package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * {@code Search} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/search} component.
 * @author Sumit
 */
@ConsumerType
public interface Search extends FacettedSearch {
    String getSearchResultsPagePath();
    String getSearchPlaceholder();
    String getDownloadFormFragmentPath();
}
