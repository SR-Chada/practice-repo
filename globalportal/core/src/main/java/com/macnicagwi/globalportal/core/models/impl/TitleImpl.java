package com.macnicagwi.globalportal.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Title;
import com.drew.lang.annotations.Nullable;
import org.apache.sling.models.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { Title.class,
        ComponentExporter.class }, resourceType = TitleImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class TitleImpl implements com.macnicagwi.globalportal.core.models.Title {

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/title";

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    protected Title title;

    @ValueMapValue
    private String pretitle;

    @ValueMapValue
    private String bulletPoint;

    @ValueMapValue
    private String underline;

    @Override
    public String getText() {
        return title.getText();
    }

    @Override
    public String getType() {
        return title.getType();
    }

    @Override
    public @Nullable String getId() {
        return title.getId();
    }

    @Override
    public String getPretitle() {
        return pretitle;
    }

    @Override
    public String getBulletPoint() {
        return bulletPoint;
    }

    @Override
    public String getUnderline() {
        return underline;
    }

    @Override
    public Link getLink() {
        return title.getLink();
    }

    @Override
    public boolean isLinkDisabled() {
        return title.isLinkDisabled();
    }

    @Override
    public String getAppliedCssClasses() {
        return title.getAppliedCssClasses();
    }

    @Override
    public String getExportedType() {
        return TitleImpl.RESOURCE_TYPE;
    }

}
