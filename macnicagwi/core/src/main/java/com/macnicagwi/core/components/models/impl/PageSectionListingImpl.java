package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_SORT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
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
import com.macnicagwi.core.components.models.PageSectionListing;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.services.JcrSearchService;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { PageSectionListing.class,
        ComponentExporter.class }, resourceType = PageSectionListingImpl.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class PageSectionListingImpl extends AbstractComponentImpl implements PageSectionListing {

    public static final String RESOURCE_TYPE = "macnicagwi/components/content/pagesectionlisting";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HASH_SYMBOL = "#";

    

    @Self
    MacnicaLinkHandler linkHandler;

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
    private String parentPage;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private boolean useHeadings;
    
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values="path")
    private String orderBy;
    
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values="asc")
    private String sortOrder;
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
        logger.debug("Initializing PageSectionListingImpl at resource {} ", resource.getPath());

        Optional<Page> rootPage;
        Optional<String> parentPath = Optional.ofNullable(this.parentPage).filter(StringUtils::isNotBlank);
        rootPage = parentPath.isPresent() ? Optional.ofNullable(pageManager.getContainingPage(parentPath.get()))
                : Optional.of(currentPage);
        if (useHeadings) {
            listItems = getHeadingListItems(rootPage);
            return;
        }
        Iterator<Page> childPages = com.macnicagwi.core.utils.MacnicaComponentUtils.getChildListItems(rootPage);
        childPages.forEachRemaining(page -> listItems.add(new PageSectionListItem(page)));
    }

    private Collection<PageSectionListItem> getHeadingListItems(Optional<Page> rootPage) {
        List<PageSectionListItem> pageHeadingsList = new ArrayList<>();
        logger.trace("Fetching heading components in page: {} ", currentPage.getPath());
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("path", currentPage.getContentResource().getPath());
        map.put("1_property", "sling:resourceType");
        map.put("1_property.value", HeadingImpl.RESOURCE_TYPE);
        map.put("2_property", "enableListing");
        map.put("2_property.value", "true");
        if(!orderBy.equalsIgnoreCase("path"))
        map.put("orderby", "@./"+orderBy);
        map.put(SEARCH_ORDER_BY_SORT, sortOrder);
        Session session = request.getResourceResolver().adaptTo(Session.class);
        List<Resource> headingResources = jcrSearchService.getResources(map, session);
        logger.info("Heading Resources size {}  for predicates {}", headingResources.size(), map);
        headingResources.forEach(resource -> {
            logger.trace("Heading resource path {} ", resource.getPath());
            String label = resource.getValueMap().get("text", String.class);
            String link = HASH_SYMBOL + resource.getValueMap().get("id", String.class);
            pageHeadingsList.add(new PageSectionListItem(label, link));
        });
        return pageHeadingsList;

    }    
   


    public class PageSectionListItem {

        private String itemLabel;

        private String itemLink;
        
        private Page page;

        public PageSectionListItem(@NotNull String itemLabel, @NotNull String itemLink) {

            this.itemLabel = itemLabel;
            this.itemLink = itemLink;
        }
        
        public PageSectionListItem(@NotNull Page page) {
        	this(page.getTitle(),page.getPath());
        	this.page = page;
        }

        public String getItemLabel() {
            return itemLabel;
        }

        public Link<Page> getItemLink() {
        	if(page!=null) {
        		return linkHandler.getLink(page).orElseGet(null);
        	} else {
        		return linkHandler.getLink(itemLink, StringUtils.EMPTY).orElseGet(null);            
            
        } }

        public String getItemHashLink() {
            return itemLink;
        }

    }

}
