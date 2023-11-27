package com.macnicagwi.core.utils;

import com.drew.lang.annotations.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.AbstractResourceVisitor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourceVisitor extends AbstractResourceVisitor {
    private static final Logger logger = LoggerFactory.getLogger(ResourceVisitor.class);

    private final List<String> paths = new ArrayList<>();

    public List<String> getPath() {
        return paths;
    }

    private final List<String> childPages = new ArrayList<>();

    public List<String> getChildPages() {
        return childPages;
    }

    @Override
    public final void accept(final Resource resource) {
        if (null != resource) {
            logger.info("-- resource.getPath() --> {}", resource.getPath());
            visit(resource);
            this.traverseChildren(resource.listChildren());
        }
    }

    @Override
    protected void traverseChildren(final @NotNull Iterator<Resource> children) {
        while (children.hasNext()) {
            final Resource child = children.next();
            accept(child);
        }
    }

    @Override
    protected void visit(@NotNull Resource resource) {
        if (resource.getResourceType().equalsIgnoreCase("cq/workflow/components/collection/definition/resource")) {
            final ValueMap properties = resource.adaptTo(ValueMap.class);
            final String path = properties.get("root", StringUtils.EMPTY);
            logger.info("-- root = --> {}", path);
            paths.add(path);
        } else {
            //if(resource.getValueMap().get("jcr:primaryType").equals("cq:PageContent") || resource.getValueMap().get("jcr:primaryType").equals("cq:Page")) {
            childPages.add(resource.getPath());
            //}
        }
    }
}