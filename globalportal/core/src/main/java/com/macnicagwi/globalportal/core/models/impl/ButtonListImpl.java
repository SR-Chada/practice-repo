package com.macnicagwi.globalportal.core.models.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.globalportal.core.models.ButtonList;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { com.adobe.cq.wcm.core.components.models.List.class,
        ComponentExporter.class }, resourceType = ButtonListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class ButtonListImpl implements ButtonList {

    static final String RESOURCE_TYPE = "globalportal/components/buttonlist";

    @ValueMapValue
    private List getListItems;

    @ValueMapValue(name = "jcr:title")
    private String title;

    @ValueMapValue
    private String type;
    
	/**
	 * The current page.
	 */
	@ScriptVariable
	private Page currentPage;

    @Self
    @Via(type = ResourceSuperType.class)
    protected com.adobe.cq.wcm.core.components.models.List list;

    public Collection<ListItem> getListItems() {
    	Collection<ListItem> items = list.getListItems();
    	return items.stream()
    	.filter(item->!StringUtils.equalsIgnoreCase(item.getPath(), currentPage.getPath()))
    			.collect(Collectors.toList());
    }

    @Override
    public String getExportedType() {
        return ButtonListImpl.RESOURCE_TYPE;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return type;
    }

}
