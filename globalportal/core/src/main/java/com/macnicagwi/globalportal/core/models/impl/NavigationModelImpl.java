package com.macnicagwi.globalportal.core.models.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Navigation;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Page;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.globalportal.core.models.NavigationModel;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { NavigationModel.class,
        ComponentExporter.class }, resourceType = NavigationModelImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NavigationModelImpl extends NavigationModel {
    private static final Logger LOG = LoggerFactory.getLogger(NavigationModelImpl.class);

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/navigation";
    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource currentResource;

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    private Navigation navigation;

    @Override
    public String getAccessibilityLabel() {
        // TODO Auto-generated method stub
        return navigation.getAccessibilityLabel();
    }

    @Override
    public List<NavigationItem> getItems() {
        // TODO Auto-generated method stub
        return navigation.getItems();
    }

    @Override
    public @Nullable String getAppliedCssClasses() {
        // TODO Auto-generated method stub
        return navigation.getAppliedCssClasses();
    }

    @Override
    public @Nullable ComponentData getData() {
        // TODO Auto-generated method stub
        return navigation.getData();
    }

    @Override
    public @Nullable String getId() {
        // TODO Auto-generated method stub
        return navigation.getId();
    }

    public ArrayList<HashMap<String, String>> getFeaturedImage() {

        ArrayList<HashMap<String, String>> featuredImageList = new ArrayList<HashMap<String, String>>();
        List<NavigationItem> pagePath = getItems();

        for (NavigationItem navigationItem : pagePath) {
            HashMap<String, String> featuredImageHash = new HashMap<String, String>();
            featuredImageHash.put("image",
                    getCurrentPageFeaturedImage(navigationItem.getLink().getURL())[0]);
            featuredImageHash.put("Alt",
                    getCurrentPageFeaturedImage(navigationItem.getLink().getURL())[1]);

            featuredImageList.add(featuredImageHash);
        }
        return featuredImageList;

    }

    protected String[] getCurrentPageFeaturedImage(String pagePath) {

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        Page currentPage = pageManager.getPage(pagePath.replace(".html", ""));

        String[] pageImage = null;
        if (currentPage != null) {

            pageImage = new String[] { currentPage.getProperties().get("./cq:featuredimage/fileReference", ""),
                    currentPage.getProperties().get("./cq:featuredimage/alt", "") };

        }
        return pageImage;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

}