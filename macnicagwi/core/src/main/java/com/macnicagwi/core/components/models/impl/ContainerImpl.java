package com.macnicagwi.core.components.models.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.Container;

@Model(
	adaptables = SlingHttpServletRequest.class, 
	adapters = { Container.class, ComponentExporter.class }, 
	resourceType = ContainerImpl.RESOURCE_TYPE, 
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
	name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, 
	extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ContainerImpl extends AbstractComponentImpl implements Container {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerImpl.class);
	public static final String RESOURCE_TYPE = "macnicagwi/components/content/form/container";

	@Self
	SlingHttpServletRequest request;

	@Self
    @Via(type = ResourceSuperType.class)
    private com.adobe.cq.wcm.core.components.models.form.Container container;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String successFragmentPath;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String errorFragmentPath;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String clientKey;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String eventId;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String childEventId;

	@PostConstruct
	public void init() {
		LOGGER.trace("Macnica GWI Form Container Compponent Initialized.");
	}

	@Override
	public String getSuccessFragmentPath() {
        return successFragmentPath;
    }

	@Override
	public String getErrorFragmentPath() {
        return errorFragmentPath;
    }

	@Override
	public String getClientKey(){
		return clientKey;
	}

	@Override
	public String getEventId(){
		return eventId;
	}

	@Override
	public String getChildEventId(){
		return childEventId;
	}

	@Override
	public String getMethod() {
		return container.getMethod();
	}
  
	@Override
	public String getAction() {
		return container.getAction();
	}
  
	@Override
	public String getId() {
		return container.getId();
	}
  
	@Override
	public String getName() {
		return container.getName();
	}
  
	@Override
	public String getEnctype() {
		return container.getEnctype();
	}
  
	@Override
	public String getResourceTypeForDropArea() {
		return container.getResourceTypeForDropArea();
	}
  
	@Override
	public String getRedirect() {
		return container.getRedirect();
	}
  
	@Nullable
	@Override
	public String[] getErrorMessages() {
		return container.getErrorMessages();
	}
  
	@NotNull
	@Override
	public String[] getExportedItemsOrder() {
		return container.getExportedItemsOrder();
	}
  
	@NotNull
	@Override
	public Map<String, ? extends ComponentExporter> getExportedItems() {
		return container.getExportedItems();
	}

	@NotNull
	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}

}
