package com.macnicagwi.core.components.models.impl;

import static com.adobe.cq.wcm.core.components.models.List.PN_TEASER_DELEGATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.NN_MANUFACTURER_LOGO;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.NN_PRODUCT_lINE_LOGO;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_ARTICLE_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_EVENT_END_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_EVENT_LOCATION;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_EVENT_REGISTRATION_END_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_EVENT_REGISTRATION_START_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_EVENT_START_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_EVENT_STATUS;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_LINK_TARGET;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.commons.ImageResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.macnicagwi.core.components.models.MacnicaListItem;
import com.macnicagwi.core.components.models.MacnicaPageListItem;
import com.macnicagwi.core.config.DateFormatConfig;
import com.macnicagwi.core.models.MacnicaLinkHandler;

public class MacnicaPageListItemImpl  implements MacnicaPageListItem {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	


	
    /**
     * Prefix prepended to the item ID.
     */
    private static final String ITEM_ID_PREFIX = "item";    
	
    private Resource resource;
    
    private Resource teaserResource;
    private Component component;
    private Map<String, String> overriddenProperties = new HashMap<>();
    private List<String> hiddenProperties = new ArrayList<>();
    private boolean showDescription =true;
    private boolean linkItems =true;

	
	 /**
     * The page for this list item.
     */
    private Page page;
    
	 /**
     * The featured image of the page
     */
    private Resource image;


    /**
     * The link for this list item.
     */
    protected Optional<Link<Page>> link;    


    /**
     * The ID of the component that contains this list item.
     */
    private String parentId;

    /**
     * The path of this list item.
     */
    private String path;
    

    
    /**
     * Construct a list item for a given page.
     *
     * @param linkHandler The link handler.
     * @param page The current page.
     * @param parentId The ID of the list containing this item.
     * @param DamAssetService The Macnica DAM Asset Service object reference.
     * @param Session JCR Session reference.
     */
    


    /**
     * Construct a list item for a given page.
     *
     * @param linkHandler The link handler.
     * @param page The current page.
     * @param parentId The ID of the list containing this item.
     */
    
    public MacnicaPageListItemImpl(@NotNull final MacnicaLinkHandler linkHandler,
                            @NotNull final Page page,
                            final String parentId) {
logger.trace("Initialising MacnicaPageListItem for page {}",page.getPath());
        
        if (resource != null) {
            this.path = resource.getPath();
        }
        this.parentId = parentId;
        this.link = linkHandler.getLink(page);
        if (this.link.isPresent()) {
            this.page = link.get().getReference();
        } else {
            this.page = page;
        }
        this.resource = page.getContentResource();
        this.image = ComponentUtils.getFeaturedImage(page);    	
    }
    
    public MacnicaPageListItemImpl(Component component,@NotNull final MacnicaLinkHandler linkHandler,
            @NotNull final Page page,final String parentId ) {
    	this(linkHandler,page,parentId);
       	this.component = component;
       	}
    
    @NotNull
    @Override
    public String getId() {
        return ComponentUtils.generateId(StringUtils.join(parentId, ComponentUtils.ID_SEPARATOR, ITEM_ID_PREFIX), path);
    }



    @Override
    @JsonIgnore
    @Nullable
    public Link<Page> getLink() {
        return link.orElse(null);
    }


    @Override
    public String getTitle() {
        return MacnicaPageListItemImpl.getTitle(this.page);
    }



 @Override
    public String getDescription() {
        return page.getDescription();
    }

    @Override
    public Calendar getLastModified() {
        return page.getLastModified();
    }

    @Override
    public String getPath() {
        return page.getPath();
    }
    
	@NotNull
	@Override
	@JsonProperty("date")
	public String getArticleDate() {
		Calendar articleDate = this.page.getProperties().get(PN_ARTICLE_DATE, Calendar.class);
		return articleDate==null?StringUtils.EMPTY:getFormattedDate(articleDate);
	}
	
    public final Resource getImage() {
    	return image;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return page.getName();
    }

    
    
    @Override
    public String getEventStartDate() {
		Calendar eventStartDate = this.page.getProperties().get(PN_EVENT_START_DATE, Calendar.class);
		return eventStartDate==null?StringUtils.EMPTY:getFormattedDate(eventStartDate);
	}
    
    @Override
    public String getEventEndDate() {
		Calendar eventEndDate = this.page.getProperties().get(PN_EVENT_END_DATE, Calendar.class);
		return eventEndDate==null?StringUtils.EMPTY:getFormattedDate(eventEndDate);
	}
	
    @Override
	public String getEventRegistrationStartDate() {
		Calendar eventRegStartDate = this.page.getProperties().get(PN_EVENT_REGISTRATION_START_DATE, Calendar.class);
		return eventRegStartDate==null?StringUtils.EMPTY:getFormattedDate(eventRegStartDate);
	}

    @Override
	public String getEventRegistrationEndDate() {
		Calendar eventRegEndDate = this.page.getProperties().get(PN_EVENT_REGISTRATION_END_DATE, Calendar.class);
		return eventRegEndDate==null?StringUtils.EMPTY:getFormattedDate(eventRegEndDate);
	}
	
	@JsonProperty("location")
	@Override
	public  String getEventLocation() {
		String location = this.page.getProperties().get(PN_EVENT_LOCATION, String.class);
		return StringUtils.isBlank(location)?StringUtils.EMPTY:location;
	}
	
	@Override
	@JsonProperty("status")
	public String getEventStatus() {
		String status = this.page.getProperties().get(PN_EVENT_STATUS, String.class);
		TagManager tagManager = page.getContentResource().getResourceResolver().adaptTo(TagManager.class);
		Locale language = this.page.getLanguage();
		status = tagManager.resolve(status).getTitle(language);
		return StringUtils.isBlank(status)?StringUtils.EMPTY:status;
	}
    
    
    /**
     * Helper method to get the title of a page list item from a given page.
     * @param page The page for which to get the title.
     * @return The list item title.
     */
    private static String getTitle(@NotNull final Page page) {
        String title = page.getNavigationTitle();
        if (title == null) {
            title = page.getPageTitle();
        }
        if (title == null) {
            title = page.getTitle();
        }
        if (title == null) {
            title = page.getName();
        }
        return title;
    }
    
    @Override
    public ItemType getItemType() {
        return MacnicaListItem.ItemType.PAGE;
    }
    
    @Override
    public Resource getProductLineLogo() {
        return page.getContentResource(NN_PRODUCT_lINE_LOGO);    
       
    }
    @Override
    public Resource getManufacturerLogo() {
        return page.getContentResource(NN_MANUFACTURER_LOGO);
    }
    
    /**
     * Converts the date configured in the resource to a formatted date based on the page.
     * @param configuredDate
     * @return formattedDate
     */
    private String getFormattedDate(@NotNull Calendar configuredDate) {
        try {
        Resource pageResource = page.adaptTo(Resource.class);
        DateFormatConfig dateFormatConfig = pageResource.adaptTo(ConfigurationBuilder.class).as(DateFormatConfig.class);
        Date date = configuredDate.getTime();
        TimeZone timeZone = configuredDate.getTimeZone();
        SimpleDateFormat formattedDate = new SimpleDateFormat(dateFormatConfig.dateFormat());
        formattedDate.setTimeZone(timeZone);
        return formattedDate.format(date);
        } catch (Exception e) {
            logger.error("Exception while converting date format ",e);
        }
        return StringUtils.EMPTY;
    }

    public String getType(){
        String type = this.page.getProperties().get(PN_TYPE,String.class);
        TagManager tagManager = page.getContentResource().getResourceResolver().adaptTo(TagManager.class);
        Locale language = this.page.getLanguage();
		type = tagManager.resolve(type).getTitle(language);
		return StringUtils.isBlank(type)?StringUtils.EMPTY:type;
        
    }
    

    public Resource getTeaserResource() {
        if (teaserResource == null && component != null) {
            String delegateResourceType = component.getProperties().get(PN_TEASER_DELEGATE, String.class);
            if (StringUtils.isEmpty(delegateResourceType)) {
            	logger.error("In order for list rendering delegation to work correctly you need to set up the teaserDelegate property on" +
                        " the {} component; its value has to point to the resource type of a teaser component.", component.getPath());
            } else {
                Resource resourceToBeWrapped = ComponentUtils.getFeaturedImage(page);
                if (resourceToBeWrapped != null) {
                    // use the page featured image and inherit properties from the page item
                    overriddenProperties.put(JcrConstants.JCR_TITLE, this.getTitle());
                    if (showDescription) {
                        overriddenProperties.put(JcrConstants.JCR_DESCRIPTION, this.getDescription());
                    }
                    if (linkItems) {
                        overriddenProperties.put(ImageResource.PN_LINK_URL, this.getPath());
                        overriddenProperties.put(PN_LINK_TARGET, page.getProperties().get(PN_LINK_TARGET,String.class));
                    }
                } else {
                    // use the page content node and inherit properties from the page item
                    resourceToBeWrapped = page.getContentResource();
                    if (resourceToBeWrapped == null) {
                        return null;
                    }
                    if (!showDescription) {
                        hiddenProperties.add(JcrConstants.JCR_DESCRIPTION);
                    }
                    if (linkItems) {
                        overriddenProperties.put(ImageResource.PN_LINK_URL, this.getPath());
                        overriddenProperties.put(PN_LINK_TARGET, page.getProperties().get(PN_LINK_TARGET,String.class));
                    }
                }
                teaserResource = new MacnicaResourceWrapper(resourceToBeWrapped, delegateResourceType, hiddenProperties, overriddenProperties);
            }
        }
        return teaserResource;
    	
    }

   

}
