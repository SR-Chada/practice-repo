package com.macnicagwi.core.components.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.macnicagwi.core.components.models.BenchmarkEmbed;
import com.adobe.cq.export.json.ExporterConstants;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {BenchmarkEmbed.class, ComponentExporter.class},
    resourceType = BenchmarkEmbedImpl.RESOURCE_TYPE
)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class BenchmarkEmbedImpl extends AbstractComponentImpl implements BenchmarkEmbed {
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/benchmarkembed";

    
    @ValueMapValue
    @Optional
    private String html;

    @Override
	public String getHtml() {
	    return html;
	}
}
