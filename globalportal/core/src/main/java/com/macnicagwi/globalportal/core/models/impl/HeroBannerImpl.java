package com.macnicagwi.globalportal.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.macnicagwi.globalportal.core.models.HeroBannerModel;

import org.apache.sling.models.annotations.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.Page;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { HeroBannerModel.class,
        ComponentExporter.class }, resourceType = HeroBannerImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class HeroBannerImpl implements HeroBannerModel {
    private static final Logger LOG = LoggerFactory.getLogger(HeroBannerImpl.class);

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/herobanner";

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource currentResource;

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private Page currentPage;

    private String pageTitle;

    @Self
    @Via(type = ResourceSuperType.class)
    protected Image image;

    @PostConstruct
    protected void init() {
        LOG.info("Current currentResource path:{}", currentResource.getPath());
        if (currentPage != null) {
            pageTitle = currentPage.getTitle();
            LOG.info("Current page title:{}", pageTitle);
        }
    }

    // Re-use the Image class for all other methods:

    @Override
    public String getSrc() {
        LOG.info("SRC:{}", image.getSrc());
        return null != image.getSrc() ? image.getSrc() : null;
    }

    @Override
    public String getSrcset() {
        LOG.info("SRC-SET:{}", image.getSrcset());
        return null != image.getSrcset() ? image.getSrcset() : null;
    }

    @Override
    public String getAlt() {
        return null != image ? image.getAlt() : null;
    }

    @Override
    public String getTitle() {
        return pageTitle;

    }

    // method required by `ComponentExporter` interface
    // exposes a JSON property named `:type` with a value of
    // `globalportal/components/herobanner`
    // required to map the JSON export to the SPA component props via the `MapTo`
    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
