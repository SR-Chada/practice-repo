package com.macnicagwi.globalportal.core.models;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.wcm.api.Page;

@ProviderType
public interface NewsListItem {

    public String getPageTitle();

    public String getDate();

    public String getPageLink();

    public List<String> getTags();

    /*
     * Returns link object of News List Item
     */
    public Link<Page> getLink();

}