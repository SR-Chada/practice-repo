package com.macnicagwi.globalportal.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;


import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.macnicagwi.globalportal.core.models.Table;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {
        ComponentExporter.class }, resourceType = TableImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class TableImpl implements Table{
	 static final String RESOURCE_TYPE = "globalportal/components/table";

	    @Self
	    @Via(type = ResourceSuperType.class)
	    protected com.adobe.cq.wcm.core.components.models.Text text;
	    
	    @Override
	    public String getText() {
	    	return text.getText();
	    }
	    
	    @Override
	    public Boolean getRichText() {
	    	return text.isRichText();
	    }
	    
	    @Override
	    public String getAppliedCssClasses() {
	    	return text.getAppliedCssClasses();
	    }
	    
	    
	    @Override
	    public String getExportedType() {
	        return TableImpl.RESOURCE_TYPE;
	    }
}
