package com.macnicagwi.globalportal.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Image;
import com.macnicagwi.globalportal.core.models.HeroCarouselSlideModel;

import org.apache.sling.models.annotations.*;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import org.apache.sling.api.resource.Resource;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { HeroCarouselSlideModel.class,
        ComponentExporter.class }, resourceType = HeroCarouselSlideImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class HeroCarouselSlideImpl implements HeroCarouselSlideModel {

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/herocarouselslide";

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource currentResource;

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    protected Image image;

    @ValueMapValue
    private String levelOneTitle;

    @ValueMapValue
    private String levelTwoTitle;

    // Re-use the Image class for all other methods:

    @Override
    public String getSrc() {
        return null != image ? image.getSrc() : null;
    }

    @Override
    public String getSrcset() {
        return null != image ? image.getSrcset() : null;
    }

    @Override
    public Link getImageLink() {
        return null != image ? image.getImageLink() : null;
    }

    @Override
    public String getAppliedCssClasses() {
        return null != image ? image.getAppliedCssClasses() : null;
    }

    @Override
    public String getId() {
        return null != image ? image.getId() : null;
    }

    @Override
    public String getAlt() {
        return null != image ? image.getAlt() : null;
    }

    @Override
    public String getLevelOneTitle() {
        return levelOneTitle;
    }

    @Override
    public String getLevelTwoTitle() {
        return levelTwoTitle;
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