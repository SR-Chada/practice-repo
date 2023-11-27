package com.macnicagwi.globalportal.core.models.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.adobe.cq.wcm.style.ComponentStyleInfo;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.globalportal.core.models.Spacer;


@Model(adaptables = SlingHttpServletRequest.class, adapters = { Spacer.class,
        ComponentExporter.class }, resourceType = SpacerImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class SpacerImpl extends AbstractComponentImpl implements Spacer {
	
	 static final String RESOURCE_TYPE = "globalportal/components/spacer";
	 
	    /**
	     * See {@link Component#getAppliedCssClasses()}
	     *
	     * @return The component styles/css class names
	     */
	    @Override
	    @Nullable
		public String getAppliedCssClasses() {

	    	return Optional.ofNullable(this.resource.adaptTo(ComponentStyleInfo.class)) 
	    			.map(ComponentStyleInfo::getAppliedCssClasses) 
	    			.filter(StringUtils::isNotBlank) 
	    			.orElse(null);		// Returning null so sling model exporters don't return anything for this property if not configured
		}

	
	

}
