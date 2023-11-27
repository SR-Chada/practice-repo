package com.macnicagwi.globalportal.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Search;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.globalportal.core.models.SearchModel;

import org.apache.sling.models.annotations.*;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { SearchModel.class,
        ComponentExporter.class }, resourceType = SearchModelImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchModelImpl implements SearchModel {
    private static final Logger LOG = LoggerFactory.getLogger(SearchModelImpl.class);

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/search";

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    private Search search;

    // Re-use the Image class for all other methods:

    @Override
    public @NotNull String getRelativePath() {
        // TODO Auto-generated method stub
        return search.getRelativePath();
    }

    @Override
    public int getResultsSize() {
        // TODO Auto-generated method stub
        return search.getResultsSize();
    }

    @Override
    public @NotNull String getSearchRootPagePath() {
        // TODO Auto-generated method stub
        return search.getSearchRootPagePath();
    }

    @Override
    public int getSearchTermMinimumLength() {
        // TODO Auto-generated method stub
        return search.getSearchTermMinimumLength();
    }

    @Override
    public @Nullable String getAppliedCssClasses() {
        // TODO Auto-generated method stub
        return search.getAppliedCssClasses();
    }

    @Override
    public @Nullable ComponentData getData() {
        // TODO Auto-generated method stub
        return search.getData();
    }

    @Override
    public @Nullable String getId() {
        // TODO Auto-generated method stub
        return search.getId();
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}