package com.macnicagwi.globalportal.core.models.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.macnicagwi.globalportal.core.models.GlobalPortalLinkHandler;
import com.macnicagwi.globalportal.core.models.TeaserList;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { com.adobe.cq.wcm.core.components.models.List.class,
        ComponentExporter.class }, resourceType = TeaserListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class TeaserListImpl extends AbstractComponentImpl implements TeaserList {

    protected static final String RESOURCE_TYPE = "globalportal/components/teaserlist";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TeaserListImpl.class);

    /**
     * Default flag indicating if list items should be displayed as teasers.
     */
    private static final boolean DISPLAY_ITEM_AS_TEASER_DEFAULT = Boolean.TRUE;
    
    private static final String LIST_TYPE_CHILDREN="children";
    
    private static final String PN_SUBTITLE="subtitle";

    /**
     * Flag indicating if items should be displayed as teasers.
     */
    private boolean displayItemAsTeaser;

    @Self
    GlobalPortalLinkHandler linkHandler;

    @Self
    @Via(type = ResourceSuperType.class)
    private com.adobe.cq.wcm.core.components.models.List list;
    
    /**
     * Component properties.
     */
    @ScriptVariable
    protected ValueMap properties;

    /**
     * The resource resolver object
     */
    @SlingObject
    private ResourceResolver resourceResolver;
    

    /**
     * Resource where component is added
     */
    @SlingObject
    private Resource currentResource;
    
    /**
     * The current page.
     */
    @ScriptVariable
    private Page currentPage;

    @Self
    private SlingHttpServletRequest request;

    /**
     * Generate list item with custom PageListItem object
     */
    @Override
    @SuppressWarnings("deprecation")
    @JsonProperty("items")
    public Collection<ListItem> getListItems() {
        ResourceResolver resourceResolver = request.getResourceResolver();
       return list.getItems().stream().map(page -> new PageListItemImpl(linkHandler, page, getId(), RESOURCE_TYPE,resourceResolver))
                .filter(Objects::nonNull).collect(Collectors.toList());
        
    }

    @Override
    public String getExportedType() {
        return TeaserListImpl.RESOURCE_TYPE;
    }

    @ValueMapValue
    private String btnReadMore;

    @ValueMapValue
    private String btnCenterOfExcellence;

    @ValueMapValue
    private Boolean solutionListingTeaser;

    @ValueMapValue
    private Boolean centerOfExcellenceTeaser;

    @ValueMapValue
    private Boolean services;

    @ValueMapValue
    private String backGroundColor;

    @ValueMapValue
    private String anchorTagLink;

    /**
     * Title of the parent page from which list items are generated
     */
    private String parentPageTitle;
    
    /**
     * Title of the parent page from which list items are generated
     */
    private String parentPageSubTitle;
    
    /**
     * description of the parent page from which list items are generated
     */
    private String parentPageDescription;
    
    /**
     * Link of the parent page from which list items are generated
     */
    private Link<Page>  parentPageLink;
    
    @PostConstruct
    private void init() {
    	LOGGER.trace("Initializing Teaser list {} ", resource.getPath());
    	String listType = properties.get(PN_SOURCE, String.class);
    	//get parent page properties if the list is built from child pages
    	if(StringUtils.equalsIgnoreCase(listType, LIST_TYPE_CHILDREN)){
    		getParentPage().ifPresent(this::setParentPageProperties);  	
    		
    	}
    	
    }

    /**
     * Get the parent page.
     *
     * The parent page is the page referenced by the 'parentPage' property, or the current page if the parent page  property
     * is not set or is blank. This function will return empty only if the parent page is configured in the component, but the referenced
     * page does not exist.
     *
     * 
     * @return The parent page, or empty if the page does not exist.
     */
    @SuppressWarnings("resource")
	@NotNull
    private Optional<Page> getParentPage() {
        Optional<String> parentPath = Optional.ofNullable(this.properties.get(PN_PARENT_PAGE, String.class))
            .filter(StringUtils::isNotBlank);

        // no path is specified, use current page
        if (!parentPath.isPresent()) {
            return Optional.of(this.currentPage);
        }

        // a path is specified, get that page or return null
        return parentPath
            .map(resource.getResourceResolver()::getResource)
            .map(currentPage.getPageManager()::getContainingPage);
    }
    /**
     * sets the parent page properties needed for teaser list
     * @param page - parent page object from which page properties are to be obtained
     */
    private void setParentPageProperties(@NotNull Page page) {
    	parentPageTitle = page.getTitle();
    	parentPageDescription = page.getDescription();
    	parentPageSubTitle = page.getProperties().get(PN_SUBTITLE, String.class);
    	parentPageLink = linkHandler.getLink(page).orElse(null);  	
    }
  

    @Override
    public String getParentPageTitle() {
        return StringUtils.isBlank(parentPageTitle)?StringUtils.EMPTY:parentPageTitle;
    }

    @Override
    public String getParentPageSubtitle() {
        return StringUtils.isBlank(parentPageSubTitle)?StringUtils.EMPTY:parentPageSubTitle;
    }

    @Override
    public String getParentPageDescription() {
        return StringUtils.isBlank(parentPageDescription)?StringUtils.EMPTY:parentPageDescription;
    }

    @Override
    public Link<Page> getParentPageLink() {
        return parentPageLink;
    }

    @Override
    public String getReadMoreButtonText() {
        return btnReadMore;
    }

    @Override
    public Boolean getsolutionListingTeaser() {
        return solutionListingTeaser;
    }

    @Override
    public Boolean getCenterOfExcellenceTeaser() {
        return centerOfExcellenceTeaser;
    }

    @Override
    public Boolean getServices() {
        return services;
    }

    @Override
    public boolean displayItemAsTeaser() {
        return DISPLAY_ITEM_AS_TEASER_DEFAULT;
    }

    @Override
    public boolean linkItems() {
        return list.linkItems();
    }

    @Override
    public boolean showDescription() {
        return list.showDescription();
    }

    @Override
    public boolean showModificationDate() {
        return list.showModificationDate();
    }

    @Override
    public String getDateFormatString() {
        return list.getDateFormatString();
    }

    @Override
    public String getBackGroundColor() {
        return backGroundColor;
    }

    @Override
    public String getCenterOfExcellenceButtonText() {
        return btnCenterOfExcellence;
    }

    @Override
    public String getAnchorTagLink(){
        return anchorTagLink;
    }

}
