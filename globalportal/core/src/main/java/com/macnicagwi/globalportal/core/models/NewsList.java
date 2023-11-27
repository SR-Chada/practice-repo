package com.macnicagwi.globalportal.core.models;

import java.util.List;

import com.adobe.cq.export.json.ComponentExporter;

public interface NewsList extends  ComponentExporter {
    public String getPagePath();

    public String getOrderBy();

    public String getSortOrder();

    public int getMaxItems();

    public String getPagination();

    public String getReadMoreButtonText();

    public String getId();

    public List<String> getTags();

}
