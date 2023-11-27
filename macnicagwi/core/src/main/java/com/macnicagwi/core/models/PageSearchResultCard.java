package com.macnicagwi.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.utils.MacnicaComponentUtils;

import lombok.Getter;

/**
 * {@code ProductLineCard} Sling Model 
 * Adaptable to Page.
 * @author Sumit
 */

@Model(adaptables = { SlingHttpServletRequest.class, Page.class })
public class PageSearchResultCard {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(PageSearchResultCard.class);
    private static final String IMAGE_PROPERTY_PATH = "cq:featuredimage@fileReference";
	
	/*Search Result Page*/
    @Self
    Page page;

    /*Search Result Type*/
    @Getter
    private String type;

    /*Search Result Page Title*/
    @Getter
    private String title;

    /*Search Result Page Description*/
    @Getter
    private String description;
    
    /*Search Result Page Image*/
    @Getter
    private String imageSrc;
    
    /*Search Result Page Path*/
    @Nullable
    @Getter
    private String redirectLink;
    
    @PostConstruct
    public void init() {
    	//To-Do: Replace this logic to inject the necessary attributes from page props
    	LOGGER.trace("Initialising Product Line Card Model: {}", page);
        this.type = NameConstants.NT_PAGE;
    	this.title = page.getTitle();
        this.description = page.getDescription();
    	this.redirectLink = page.getPath();
    	//To-Do: Change logic to return featured image
    	this.imageSrc = MacnicaComponentUtils.getPagePropertyValue(page, IMAGE_PROPERTY_PATH);   		
    }
}
