package com.macnicagwi.globalportal.core.models.impl;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.globalportal.core.models.PageSectionListing;
import com.macnicagwi.globalportal.core.models.GlobalPortalLinkHandler;
import com.macnicagwi.globalportal.core.services.JcrSearchService;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { PageSectionListing.class,
        ComponentExporter.class }, resourceType = PageSectionListingImpl.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class PageSectionListingImpl extends AbstractComponentImpl implements PageSectionListing {

    public static final String RESOURCE_TYPE = "globalportal/components/pagesectionlisting";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HASH_SYMBOL = "#";

    @Self
    GlobalPortalLinkHandler linkHandler;

    /**
     * Injecting the QueryBuilder dependency
     */
    @Reference
    private QueryBuilder builder;

    /**
     * The current page.
     */
    @ScriptVariable
    private Page currentPage;

    /**
     * Page Manager reference
     */
    @ScriptVariable
    private PageManager pageManager;

    /**
     * Model factory OSGI service reference
     */

    @OSGiService
    private ModelFactory modelFactory;

    @OSGiService
    private JcrSearchService jcrSearchService;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private boolean useHeadings;
    /**
     * Result list.
     */
    @Nullable
    private Collection<PageSectionListItem> listItems = new ArrayList<>();

    @Override
    public Collection<PageSectionListItem> getListItems() {
        return listItems;
    }

    @PostConstruct
    private void init() {
        if (useHeadings) {
            listItems = getHeadingListItems();
        }
    }

    private Collection<PageSectionListItem> getHeadingListItems() {
        List<PageSectionListItem> pageHeadingsList = new ArrayList<>();
        logger.trace("Fetching heading components in page: {} ",
                currentPage.getPath());
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("path", currentPage.getContentResource().getPath());
        map.put("1_property", "sling:resourceType");
        map.put("1_property.value", TitleImpl.RESOURCE_TYPE);
        map.put("2_property", "enableListing");
        map.put("2_property.value", "true");
        map.put("orderby", "id");
        Session session = request.getResourceResolver().adaptTo(Session.class);
        List<Resource> headingResources = jcrSearchService.getResources(map,
                session);
        headingResources.forEach(resource -> {
            String label = resource.getValueMap().get("jcr:title", String.class);
            int link =  Integer.parseInt(resource.getValueMap().get("id", String.class));
            pageHeadingsList.add(new PageSectionListItem(label, link));
        });
        // Compare the objects based on the integer value first
        // If the integer value is the same, compare based on the string value
        Collections.sort(pageHeadingsList, Comparator.comparingInt(PageSectionListItem::getItemHashLink).thenComparing(PageSectionListItem::getItemLabel));
        return pageHeadingsList;

    }

    public class PageSectionListItem {

        private String itemLabel;

        private int itemLink;

        public PageSectionListItem(@NotNull String itemLabel, @NotNull int itemLink) {

            this.itemLabel = itemLabel;
            this.itemLink = itemLink;
        }

        public String getItemLabel() {
            return itemLabel;
        }

        public Link<Page> getItemLink() {
            Optional<Link<Page>> pageLink = linkHandler.getLink(HASH_SYMBOL+Integer.toString(itemLink),"");
            return pageLink.orElseGet(null);
        }

        public int getItemHashLink() {
            return itemLink;
        }

    }

    @Override
    public String getExportedType() {
        return PageSectionListingImpl.RESOURCE_TYPE;
    }

}
