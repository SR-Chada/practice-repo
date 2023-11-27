package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.PAGE_TYPE_MANUFACTURER_LANDING;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PAGE_TYPE_PRODUCT_LINE_DETAIL;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_PAGE_TYPE;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.macnicagwi.core.components.models.MacnicaPageListItem;
import com.macnicagwi.core.components.models.ProductLineList;
import com.macnicagwi.core.models.MacnicaLinkHandler;

import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { ProductLineList.class,
        ComponentExporter.class }, resourceType = ProductLineListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProductLineListImpl extends AbstractComponentImpl implements ProductLineList {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProductLineListImpl.class);

    /**
     * The resource type.
     */
    protected static final String RESOURCE_TYPE = "macnicagwi/components/content/productlinelist";


    /**
     * The page path configured in the component
     */

    @ValueMapValue
    @Optional
    private String rootPath;

    /**
     * The current page.
     */
    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private ResourceResolver resourceResolver;

    /**
     * The list of product line pages
     */
    @Getter
    private Collection<MacnicaPageListItem> listItems;
    
    @Self
    MacnicaLinkHandler linkHandler;

    /**
     * Initialize the model.
     */

    @PostConstruct
    protected void init() {
        //Initializing list items to empty collection. 
        listItems = Collections.emptyList();
        LOGGER.trace("Product Line Listing Component Model Initialized at path {}", resource.getPath());        
        // If parent page is not authored, current page is considered as parent page
        if (StringUtils.isBlank(rootPath)) {
            rootPath = currentPage.getPath();
        }
        //add condition to check if page is of type manufacturer detail. 
        
        java.util.Optional<Page>  manufacturerPage = java.util.Optional.ofNullable(rootPath).map(resource.getResourceResolver()::getResource).map(currentPage.getPageManager()::getContainingPage);
        if(manufacturerPage.isPresent()&& isManufacturerPage(manufacturerPage.get())) {  
            listItems =StreamSupport.stream(((Iterable<Page>) () -> manufacturerPage.get().listChildren()).spliterator(), false)
            .filter(ProductLineListImpl::isProductLinePage)
            .map(productLine -> new MacnicaPageListItemImpl(component,linkHandler, productLine, getId())).collect(Collectors.toList()); 
        }
        
    }
    
    private static boolean isManufacturerPage(@NotNull Page page) {
        String pageType = page.getContentResource().getValueMap().get(PN_PAGE_TYPE, String.class);
        LOGGER.warn("Provided page {} is not a Manufacturer landing page",page.getPath());
        return StringUtils.equalsIgnoreCase(pageType, PAGE_TYPE_MANUFACTURER_LANDING);
    }

    /**
     * Determines if a given page is a product line detail page
     * 
     * @param pagePath
     * @return true, if the page has the {@code } property in the JCR Content node ;
     *         False otherwise
     */
    protected static boolean isProductLinePage(@NotNull Page page) {        
        String pageType = page.getContentResource().getValueMap().get(PN_PAGE_TYPE, String.class);
        LOGGER.warn("Provided page {} is not a product line detail page",page.getPath());
        return StringUtils.equalsIgnoreCase(pageType, PAGE_TYPE_PRODUCT_LINE_DETAIL);
       
    }
    
  



   

}
