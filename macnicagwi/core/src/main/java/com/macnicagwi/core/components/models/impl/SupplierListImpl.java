package com.macnicagwi.core.components.models.impl;

import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.macnicagwi.core.components.models.SupplierList;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_OFFSET;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
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
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.MacnicaPageListItem;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.models.MacnicaPredicateHandler;
import com.macnicagwi.core.services.JcrSearchService;
import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { SupplierList.class,
        ComponentExporter.class }, resourceType = SupplierListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class SupplierListImpl extends AbstractComponentImpl implements SupplierList{

    public static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerListImpl.class);
    public static final String RESOURCE_TYPE = "macnicagwi/components/content/supplierlist";
    public static final String PAGE_TYPE_PROPERTY_PATH = "jcr:content/pageType";
    public static final String MANUFACTURER_PAGE_RANKING_PROPERTY_PATH = "jcr:content/productManufacturerRanking";

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

    @ValueMapValue
    @Nullable
    private String buttonText;

    @ValueMapValue
    @Nullable
    private String selectCompanyText;
        
    @Inject
    @Getter
    private Integer maxItems;
   
    @Getter
    private List<MacnicaPageListItem> listItems;

    @Getter
    private Integer totalResultsSize;

    @OSGiService
    private JcrSearchService searchService;

    @Getter
    private String resourcePath; 
    
    private List<MacnicaPageListItem> pagetitles;

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
        
    public Map<String, List<MacnicaPageListItem>> getPagetitles() {
        Map<String, String> predicates = predicateHandler.getManufacturerListTitlePredicates();
        pagetitles = generateListItems(predicates);
        pagetitles.sort(Comparator.comparing(MacnicaPageListItem::getTitle, String.CASE_INSENSITIVE_ORDER));
        Map<String, List<MacnicaPageListItem>> groupedTitles = new LinkedHashMap<>();

        for(MacnicaPageListItem item : pagetitles){
            String title = item.getTitle();
            String firstLetter = title.substring(0, 1).toUpperCase();

            List<MacnicaPageListItem> titlesForLetter = groupedTitles.computeIfAbsent(firstLetter, k -> new ArrayList<>());
            titlesForLetter.add(item);
        }
        return groupedTitles;
    }

     /**
     * Helper method to generate card list items based on rootPath and Facets
     * 
     * @param rootPath: Root page path for generating items list
     * @param facetMap
     */
    private List<MacnicaPageListItem> generateListItems(
        @Nullable Map<String, String> predicates) {
    LOGGER.trace("Generating Manufacturer list items for predicates {} ", predicates.entrySet());
    if (predicates.isEmpty()) {
        LOGGER.warn("No search predicates supplied to generate Manufacturer list items. Returning empty list");
        return Collections.emptyList();
    }
    // JCR lookup to build card list items
    LOGGER.trace("Executing query to fetch pagelist for building Manufacturer list items");
    Session session = resource.getResourceResolver().adaptTo(Session.class);
    if (searchService == null || session == null) {
        LOGGER.error("Unable to generate Manufacturer list items. session : {} searchService {}", session, searchService);
        return Collections.emptyList();
    }

    List<Page> manufacturerDetailPages = searchService.getPages(predicates, session);
    predicates.remove(SEARCH_OFFSET);
    predicates.remove(SEARCH_LIMIT);
    this.totalResultsSize = this.searchService.getTotalCount(predicates, session);
    if (!manufacturerDetailPages.isEmpty()) {
        List<MacnicaPageListItem> listItems = new ArrayList<>();
        manufacturerDetailPages.forEach(manufacturerDetailPage -> {
            MacnicaPageListItem manufacturerDetailPageItem = new MacnicaPageListItemImpl(linkHandler,
                    manufacturerDetailPage, getId());
            listItems.add(manufacturerDetailPageItem);
        });
        return listItems;
    }

    LOGGER.warn("No Manufacturer results found");

    return Collections.emptyList();
}

    @Override
    public String getButtonText() {
        return buttonText;
    }

    @Override
    public String getSelectCompanyText() {
    return selectCompanyText;
    }
}
