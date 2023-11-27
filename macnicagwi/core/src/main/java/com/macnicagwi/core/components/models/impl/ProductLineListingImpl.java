package com.macnicagwi.core.components.models.impl;

import javax.annotation.PostConstruct;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.macnicagwi.core.components.models.ProductLineListing;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { ProductLineListing.class,
    ComponentExporter.class }, resourceType = ProductLineListingImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProductLineListingImpl extends FacettedSearchImpl implements ProductLineListing {
    public static final Logger LOGGER = LoggerFactory.getLogger(ProductLineListingImpl.class);
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/productlinelisting";

    @PostConstruct
    @Override
    public void init() {
        super.init();
        LOGGER.trace("Product Line Listing Component Model Initialized.");
    }

}
