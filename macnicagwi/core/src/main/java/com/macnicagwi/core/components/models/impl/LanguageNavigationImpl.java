package com.macnicagwi.core.components.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.LanguageNavigation;
import com.adobe.cq.wcm.core.components.models.LanguageNavigationItem;
import com.adobe.cq.wcm.core.components.models.NavigationItem;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.designer.Style;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.models.MacnicaLanguageNavigationItemImpl;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.osgi.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, adapters = {LanguageNavigation.class}, resourceType = LanguageNavigationImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class LanguageNavigationImpl extends AbstractComponentImpl implements com.adobe.cq.wcm.core.components.models.LanguageNavigation{
    private static final Logger LOG = LoggerFactory.getLogger(LanguageNavigationImpl.class);

     // points to the the component resource path in ui.apps
     static final String RESOURCE_TYPE = "macnicagwi/components/content/languagenavigation";
     private static final String PN_ACCESSIBILITY_LABEL = "accessibilityLabel";
    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private ValueMap properties;

    @Self
    @Via(type = ResourceSuperType.class)
    protected LanguageNavigation languageNavigation;
    
    @Self
    MacnicaLinkHandler linkHandler;

    @ScriptVariable
    private Page currentPage;

    @ScriptVariable
    private Style currentStyle;

    @Nullable
    private String accessibilityLabel;

    private String navigationRoot;
    private int structureDepth;
    private Page rootPage;
    private List<NavigationItem> items;
    private int startLevel;

    @PostConstruct
    private void initModel() {
        navigationRoot = properties.get(PN_NAVIGATION_ROOT, currentStyle.get(PN_NAVIGATION_ROOT, String.class));
        structureDepth = properties.get(PN_STRUCTURE_DEPTH, currentStyle.get(PN_STRUCTURE_DEPTH, 1));
    }

    @Override
    public List<NavigationItem> getItems() {
        if (items == null) {
            PageManager pageManager = currentPage.getPageManager();
            rootPage = pageManager.getPage(navigationRoot);
            if (rootPage != null) {
                int rootPageLevel = rootPage.getDepth();
                startLevel = rootPageLevel + 1;
                structureDepth += rootPageLevel;
                items = getItems(rootPage);
            } else {
                items = Collections.emptyList();
            }
        }
        return Collections.unmodifiableList(items);
    }

    @Override
    @Nullable
    public String getAccessibilityLabel() {
        if (this.accessibilityLabel == null) {
            this.accessibilityLabel = this.resource.getValueMap().get(PN_ACCESSIBILITY_LABEL, String.class);
        }
        return this.accessibilityLabel;
    }

    @NotNull
    @Override
    public String getExportedType() {
        return request.getResource().getResourceType();
    }

    private List<NavigationItem> getItems(Page root) {
        List<NavigationItem> pages = new ArrayList<>();
        if (root.getDepth() < structureDepth) {
            Iterator<Page> it = root.listChildren(new PageFilter());
            while (it.hasNext()) {
                Page page = it.next();
                boolean active = currentPage.getPath().equals(page.getPath()) || currentPage.getPath().startsWith(page.getPath() + "/");
                String title = page.getNavigationTitle();
                if (title == null) {
                    title = page.getTitle();
                }
                List<NavigationItem> children = getItems(page);
                int level = page.getDepth() - startLevel;
                Page localizedPage = getLocalizedPage(currentPage, page);
                if (localizedPage != null) {
                    page = localizedPage;
                }
                boolean current = currentPage.getPath().equals(page.getPath());
                pages.add(newLanguageNavigationItem(page, active, current, linkHandler, level, children, title, getId(), component));
            }
        }

        return pages;
    }

    protected LanguageNavigationItem newLanguageNavigationItem(Page page, boolean active, boolean current, @NotNull MacnicaLinkHandler linkHandler,
                                                               int level, List<NavigationItem> children, String title, String parentId,
                                                               Component component) {
        return new MacnicaLanguageNavigationItemImpl(page, active, current, linkHandler, level, children, title, parentId, component);
    }

    private Page getLocalizedPage(Page page, Page languageRoot) {
        Page localizedPage;
        String path = languageRoot.getPath();
        String relativePath = page.getPath();
        if (relativePath.startsWith(path + "/")) {
            localizedPage = page;
        } else {
            String separator = "/";
            int i = relativePath.indexOf(separator);
            int occurrence = StringUtils.countMatches(path, separator) + 1;
            while (--occurrence > 0 && i != -1) {
                i = relativePath.indexOf(separator, i + 1);
            }
            relativePath = (i > 0) ? relativePath.substring(i) : "";
            path = path.concat(relativePath);
            PageManager pageManager = page.getPageManager();
            localizedPage = pageManager.getPage(path);
        }
        return localizedPage;
    }
}
