package com.macnicagwi.core.components.models.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.Heading;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {Heading.class, ComponentExporter.class},
    resourceType = HeadingImpl.RESOURCE_TYPE
)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class HeadingImpl extends AbstractComponentImpl implements Heading {
	public static final String RESOURCE_TYPE = "macnicagwi/components/content/heading";
	
	 @Self
	    private SlingHttpServletRequest request;

	    @ScriptVariable
	    private Resource resource;

	    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    @Nullable
	    private String text;
	    
	    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    @Nullable
	    private String type;

	    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    @Nullable
	    private String icon;
	    
	    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    @Nullable
	    private String cssClassName;
	    
	    @Override
	    public String getText() {
	        return text;
	    }
	    
	    @Override
	    public String getType() {
	        return type;
	    }
	    
	    @Override
	    @Nullable
	    public String getIcon() {
	        return icon;
	    }
	    
	    @Override
	    @NotNull
	    public String getCssClassName() {
	    	if(StringUtils.isBlank(cssClassName)){
	    		return StringUtils.EMPTY;
	    	}
	        return cssClassName;
	    }
	    
	    
	    @NotNull
	    @Override
	    public String getExportedType() {
	        return request.getResource().getResourceType();
	    }

	    @Override
	    @NotNull
	    protected ComponentData getComponentData() {
	        return DataLayerBuilder.extending(super.getComponentData()).asComponent()
	            .withText(this::getText)
	            .build();
	    }
	    
	    
	    

	    

}
