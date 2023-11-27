package com.macnicagwi.globalportal.core.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.List;
import com.day.cq.wcm.api.Page;

public interface TeaserList extends List {

    public String getReadMoreButtonText();

    public String getCenterOfExcellenceButtonText();

    public String getParentPageTitle();

    public String getParentPageSubtitle();

    public String getParentPageDescription();

    public Boolean getsolutionListingTeaser();

    public Boolean getCenterOfExcellenceTeaser();

    public Boolean getServices();

    public Link<Page> getParentPageLink();

    public String getBackGroundColor();

    public String getAnchorTagLink();

}
