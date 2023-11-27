package com.macnicagwi.core.components.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.ImageText;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {ImageText.class, ComponentExporter.class},
    resourceType = ImageTextImpl.RESOURCE_TYPE
)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ImageTextImpl extends AbstractComponentImpl implements ImageText {
	public static final String RESOURCE_TYPE = "macnicagwi/components/content/imagetext";
	
	    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    @Nullable
	    private boolean isText;
	    
	    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    private String assetType;

		@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    @Nullable
	    private boolean isHeading;

	    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    @Nullable
	    private boolean isButton;

		@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    private String assetPositionLargeScreen;
	    
		@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    private String assetPositionMediumScreen;

		@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	    private String assetPositionSmallScreen;

	    @Override
	    public boolean isText() {
	        return isText;
	    }
	    
	    @Override
	    public String getAssetType() {
	        return assetType;
	    }
		
		@Override
	    public boolean isHeading() {
	        return isHeading;
	    }
	    
	    @Override
	    @Nullable
	    public boolean isButton() {
	        return isButton;
	    }
	    
		@Override
		public String getAssetPositionLargeScreen() {
			return assetPositionLargeScreen;
		}
	    
		@Override
		public String getAssetPositionMediumScreen() {
			return assetPositionMediumScreen;
		}
	    
		@Override
		public String getAssetPositionSmallScreen() {
			return assetPositionSmallScreen;
		}

	    @NotNull
	    @Override
	    public String getExportedType() {
	        return request.getResource().getResourceType();
	    }

	    

}
