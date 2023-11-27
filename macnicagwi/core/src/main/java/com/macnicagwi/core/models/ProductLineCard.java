package com.macnicagwi.core.models;

import javax.annotation.PostConstruct;

import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.utils.MacnicaComponentUtils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * {@code ProductLineCard} Sling Model 
 * Adaptable to Page.
 * @author Sumit
 */

@Model(adaptables = { SlingHttpServletRequest.class, Page.class })
public class ProductLineCard {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductLineCard.class);
    private static final String IMAGE_PROPERTY_PATH = "cq:featuredimage@fileReference";
	
	/*Product Line page*/
    @Self
    Page page;

    /*Product Line Title*/
    @NotNull
    @Getter
    private String title;
    
    /*Product Line Image*/
    @NotNull
    @Getter
    private String imageSrc;
    
    /*Product line details page path*/
    @Nullable
    @Getter
    private String redirectLink;
    
    @PostConstruct
    public void init() {
    	//To-Do: Replace this logic to inject the necessary attributes from page props
    	LOGGER.trace("Initialising Product Line Card Model: {}", page);
    	this.title = page.getTitle();
    	this.redirectLink = page.getPath();
    	//To-Do: Change logic to return featured image
    	this.imageSrc = MacnicaComponentUtils.getPagePropertyValue(page, IMAGE_PROPERTY_PATH);   		
    }
}
