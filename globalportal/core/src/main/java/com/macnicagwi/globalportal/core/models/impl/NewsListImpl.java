package com.macnicagwi.globalportal.core.models.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.macnicagwi.globalportal.core.models.NewsList;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class }, adapters = { NewsList.class,
        ComponentExporter.class }, resourceType = NewsListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NewsListImpl implements NewsList {

    static final String RESOURCE_TYPE = "globalportal/components/newslist";

    @Inject
    private String pagePath;

    @Inject
    private String orderBy;

    @Inject
    private String sortOrder;

    @Inject
    private int maxItems;

    @Inject
    private String readMoreButtonText;

    @Inject
    private String id;

    @Inject
    private String pagination;

    @Inject
    private List<String> tags;

    @Override
    public String getPagePath() {
        return null != pagePath ? pagePath : null;
    }

    @Override
    public String getOrderBy() {
        return null != orderBy ? orderBy : null;
    }

    @Override
    public String getSortOrder() {
        return null != sortOrder ? sortOrder : null;
    }

    @Override
    public int getMaxItems() {
        return maxItems != 0 ? maxItems : 0;
    }

    @Override
    public String getPagination() {
        return null != pagination ? pagination : null;
    }

    @Override
    public String getId() {
        return null != id ? id : null;
    }

    @Override
    public List<String> getTags() {
        return null != tags ? tags : null;
    }

    @Override
    public String getReadMoreButtonText() {
        return null != readMoreButtonText ? readMoreButtonText : null;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

}
