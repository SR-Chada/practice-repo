package com.macnicagwi.globalportal.core.models.impl;

import org.apache.sling.models.annotations.*;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.macnicagwi.globalportal.core.models.ParticleModel;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { ParticleModel.class,
        ComponentExporter.class }, resourceType = ParticleModelImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ParticleModelImpl implements ParticleModel {

    // points to the the component resource path in ui.apps
    static final String RESOURCE_TYPE = "globalportal/components/particle";

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
