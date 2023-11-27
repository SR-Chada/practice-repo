package com.macnicagwi.globalportal.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.globalportal.core.models.Button;

import org.apache.sling.models.annotations.*;

import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import com.macnicagwi.globalportal.core.models.GlobalPortalLinkHandler;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { com.adobe.cq.wcm.core.components.models.Button.class,
        ComponentExporter.class }, resourceType = ButtonImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ButtonImpl implements Button {
    private static final Logger LOG = LoggerFactory.getLogger(ButtonImpl.class);

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/button";

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    protected com.adobe.cq.wcm.core.components.models.Button button;

    @SlingObject
    private Resource resource;

    @Self
    private GlobalPortalLinkHandler linkHandler;
    private Optional<Link> link;

    @PostConstruct
    private void initModel() {
        link = linkHandler.getLink(resource, "linkURL");
    }


    @Override
    public String getAccessibilityLabel() {
        return button.getAccessibilityLabel();
    }

    @Override
    public Link getButtonLink() {
         LOG.trace("Button link: ", link); 
        return link.orElse(null);
    }

    @Override
    public String getIcon() {
        return button.getIcon();
    }

    @Override
    public String getText() {
        return button.getText();
    }

    @Override
    public @Nullable String getAppliedCssClasses() {
        return button.getAppliedCssClasses();
    }

    @Override
    public @Nullable ComponentData getData() {
        return button.getData();
    }

    @Override
    public @Nullable String getId() {
        return button.getId();
    }

    @Override
    public String getExportedType() {
        return ButtonImpl.RESOURCE_TYPE;
    }
}
