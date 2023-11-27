/**
 * 
 */
package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_OFFSET;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.LanguageManager;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.MacnicaPageListItem;
import com.macnicagwi.core.components.models.ManufacturerList;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.models.MacnicaPredicateHandler;
import com.macnicagwi.core.services.DamAssetService;
import com.macnicagwi.core.services.JcrSearchService;
import com.macnicagwi.core.services.MacnicaFacetSearchService;
import com.macnicagwi.core.utils.MacnicaComponentUtils;

import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { ManufacturerList.class,
        ComponentExporter.class }, resourceType = ManufacturerListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ManufacturerListImpl extends AbstractComponentImpl implements ManufacturerList {

    public static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerListImpl.class);
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/manufacturerlist";
    public static final String PAGE_TYPE_PROPERTY_PATH = "jcr:content/pageType";
    public static final String MANUFACTURER_PAGE_RANKING_PROPERTY_PATH = "jcr:content/productManufacturerRanking";
    private static final String MANUFACTURER_PAGE_TYPE = "productManufacturerLandingPage";

    @Self
    private MacnicaLinkHandler linkHandler;

    @Self
    private MacnicaPredicateHandler predicateHandler;

    @OSGiService
    @Required
    private QueryBuilder queryBuilder;

    @ScriptVariable
    private Page currentPage;

    @ValueMapValue
    @Nullable
    private String rootPath;
    
    @Inject
    @Getter
    private Integer maxItems;

    @Getter
    private Map<MacnicaPageListItem, List<MacnicaPageListItem>> listItems;

    @Getter
    private Integer totalResultsSize;

    @OSGiService
    private JcrSearchService searchService;

    @Getter
    private String resourcePath;

    /**
     * Initialize component
     */
    @PostConstruct
    protected void init() {
        try {
            
        resourcePath = request.getResource().getPath();
        LOGGER.trace("Initializing Manufacturer List component. Resource path {} ", resourcePath);
        Map<String, String> predicates = predicateHandler.getManufacturerListPredicates();
        listItems = generateListItems(predicates);
        } catch (Exception e) {
            LOGGER.error("Unable to initialize manufacturer listing component. Error: ",e);
        }

    }

    /**
     * Helper method to generate card list items based on rootPath and Facets
     * 
     * @param rootPath: Root page path for generating items list
     * @param facetMap
     */
    private Map<MacnicaPageListItem, List<MacnicaPageListItem>> generateListItems(
            @Nullable Map<String, String> predicates) {
        LOGGER.trace("Generating Manufacturer list items for predicates {} ", predicates.entrySet());
        if (predicates.isEmpty()) {
            LOGGER.warn("No search predicates supplied to generate Manufacturer lsit items. Returning empty list");
            return Collections.emptyMap();
        }
        // JCR lookup to build card list items
        LOGGER.trace("Executing query to fetch pagelist for builing Manufacturer list items");
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        if (searchService == null || session == null) {
            LOGGER.error(
                    "Unable to generate Manufacturer list items. session : {} searchService {}",
                    session, searchService);
            return Collections.emptyMap();
        }

        List<Page> manufacturerDetailPages = searchService.getPages(predicates, session);
        predicates.remove(SEARCH_OFFSET);
        predicates.remove(SEARCH_LIMIT);
        this.totalResultsSize = this.searchService.getTotalCount(predicates, session);
        if (!manufacturerDetailPages.isEmpty()) {
            Map<MacnicaPageListItem, List<MacnicaPageListItem>> listItems = new LinkedHashMap<>();
            manufacturerDetailPages.forEach(manufacturerDetailPage -> {
                MacnicaPageListItem manufacturerDetailPageItem = new MacnicaPageListItemImpl(linkHandler,
                        manufacturerDetailPage, getId());
                List<MacnicaPageListItem> productLinePageItems = getProductLinePageItems(manufacturerDetailPage);
                listItems.put(manufacturerDetailPageItem, productLinePageItems);
            });
            return listItems;
        }

        LOGGER.warn("No Manufacturer results found");

        return Collections.emptyMap();

    }
    
    /**
     * Generate MacnicaPageListItem Objects for all ProductLineDetail Pages for a given manufacturer detail page
     * @param manufacturerDetailPage
     * @return List of ProductLineDetailPageListItems
     */

    private final List<MacnicaPageListItem> getProductLinePageItems(Page manufacturerDetailPage) {
        Iterator<Page> productLineIterator =MacnicaComponentUtils.getChildListItems(Optional.of(manufacturerDetailPage));        
        return StreamSupport.stream(((Iterable<Page>) () -> productLineIterator).spliterator(), false)
                .filter(Objects::nonNull)
                .filter(ProductLineListImpl::isProductLinePage) //filter our product line pages
                .map(page -> new MacnicaPageListItemImpl(linkHandler,page,getId())).collect(Collectors.toList());
       
    }

}
