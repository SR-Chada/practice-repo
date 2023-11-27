package com.macnicagwi.globalportal.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.macnicagwi.globalportal.core.models.Carousel;

import org.apache.sling.models.annotations.*;

import java.util.List;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { com.adobe.cq.wcm.core.components.models.Carousel.class,
        ComponentExporter.class }, resourceType = CarouselImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CarouselImpl implements Carousel {

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/carousel";

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    protected com.adobe.cq.wcm.core.components.models.Carousel carousel;

    @ValueMapValue
    private String scrollText;

    @Override
    public String getAccessibilityLabel() {
        return carousel.getAccessibilityLabel();
    }

    @Override
    public boolean getAutopauseDisabled() {
        return carousel.getAutopauseDisabled();
    }

    @Override
    public boolean getAutoplay() {
        return carousel.getAutoplay();
    }

    @Override
    public Long getDelay() {
        return carousel.getDelay();
    }

    @Override
    public boolean isControlsPrepended() {
        return carousel.isControlsPrepended();
    }

    @Override
    public String getBackgroundStyle() {
        return carousel.getBackgroundStyle();
    }

    @Override
    public Map<String, ? extends ComponentExporter> getExportedItems() {
        return carousel.getExportedItems();
    }

    @Override
    public String[] getExportedItemsOrder() {
        return carousel.getExportedItemsOrder();
    }

    @Override
    public List<ListItem> getItems() {
        return carousel.getItems();
    }

    @Override
    public String getAppliedCssClasses() {
        return carousel.getAppliedCssClasses();
    }

    @Override
    public ComponentData getData() {
        return carousel.getData();
    }

    @Override
    public String getId() {
        return carousel.getId();
    }

    // returns scroll button text authored from dialog
    public String getScrollText() {
        return scrollText;
    }

    @Override
    public String getExportedType() {
        return CarouselImpl.RESOURCE_TYPE;
    }
}
