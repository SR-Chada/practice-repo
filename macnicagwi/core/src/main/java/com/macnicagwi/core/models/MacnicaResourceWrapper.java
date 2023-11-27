package com.macnicagwi.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Exporter;

import com.adobe.cq.export.json.ExporterConstants;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME , extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class MacnicaResourceWrapper extends ResourceWrapper {

	private ValueMap valueMap;
    private String overriddenResourceType;
    private Map<String, Resource> overriddenChildren;

    public MacnicaResourceWrapper(@NotNull Resource resource, @NotNull String overriddenResourceType) {
        this(resource, overriddenResourceType, null, null, null);
    }

    public MacnicaResourceWrapper(@NotNull Resource resource, @NotNull String overriddenResourceType,
                               @Nullable List<String> hiddenProperties, @Nullable Map<String, String> overriddenProperties) {
        this(resource, overriddenResourceType, hiddenProperties, overriddenProperties, null);
    }

    public MacnicaResourceWrapper(@NotNull Resource resource, @NotNull String overriddenResourceType,
                               @Nullable List<String> hiddenProperties, @Nullable Map<String, String> overriddenProperties,
                               @Nullable Map<String, Resource> overriddenChildren) {
        super(resource);
        if (StringUtils.isEmpty(overriddenResourceType)) {
            throw new IllegalArgumentException("The " + MacnicaResourceWrapper.class.getName() + " needs to override the resource type of " +
                    "the wrapped resource, but the resourceType argument was null or empty.");
        }
        this.overriddenResourceType = overriddenResourceType;
        this.overriddenChildren = overriddenChildren;
        valueMap = new ValueMapDecorator(new HashMap<>(resource.getValueMap()));
        valueMap.put(ResourceResolver.PROPERTY_RESOURCE_TYPE, overriddenResourceType);
        if (overriddenProperties != null) {
            for (Map.Entry<String, String> entry : overriddenProperties.entrySet()) {
                valueMap.put(entry.getKey(), entry.getValue());
            }
        }
        if (hiddenProperties != null) {
            for (String property : hiddenProperties) {
                valueMap.remove(property);
            }
        }
    }

    @Override
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        if (type == ValueMap.class) {
            return (AdapterType) valueMap;
        }
        return super.adaptTo(type);
    }

    @Override
    @NotNull
    public ValueMap getValueMap() {
        return valueMap;
    }

    @Override
    @Nullable
    public Resource getChild(String relPath) {
        if (overriddenChildren != null && overriddenChildren.containsKey(relPath)) {
            
                return overriddenChildren.get(relPath);
            
        }
        return super.getChild(relPath);
    }

    @Override
    @NotNull
    public String getResourceType() {
        return overriddenResourceType;
    }

    @Override
    public boolean isResourceType(String resourceType) {
        return this.getResourceResolver().isResourceType(this, resourceType);
    }

}
