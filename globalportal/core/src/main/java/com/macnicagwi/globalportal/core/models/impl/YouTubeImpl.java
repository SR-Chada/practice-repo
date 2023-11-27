package com.macnicagwi.globalportal.core.models.impl;

import java.net.URISyntaxException;

import javax.annotation.Resource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.macnicagwi.globalportal.core.models.Youtube;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, adapters = { Youtube.class,
        ComponentExporter.class }, resourceType = YouTubeImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class YouTubeImpl extends AbstractComponentImpl implements Youtube {

    public static final String RESOURCE_TYPE = "globalportal/components/embed";

    @Self
    @Via(type = ResourceSuperType.class)
    protected com.adobe.cq.wcm.core.components.models.embeddable.YouTube youTube;

    public String getYoutube() throws URISyntaxException {
        return youTube.getIFrameSrc();
    }

    @Override
    public String getIFrameAspectRatio() {
        return youTube.getIFrameAspectRatio();
    }

    @Override
    public String getIFrameHeight() {
        return youTube.getIFrameHeight();
    }

    @Override
    public String getIFrameSrc() throws URISyntaxException {
        return youTube.getIFrameSrc();
    }

    @Override
    public String getIFrameWidth() {
        return youTube.getIFrameWidth();
    }

    @Override
    public String getLayout() {
        return youTube.getLayout();
    }

    @Override
    public String getExportedType() {
        return YouTubeImpl.RESOURCE_TYPE;
    }

}
