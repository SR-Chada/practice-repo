package com.macnicagwi.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
 * {@code ManufacturerCard} Sling Model 
 * Adaptable to Page.
 * @author Sumit
 */
@Model(adaptables = { SlingHttpServletRequest.class, Page.class })
public class ManufacturerCard {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerCard.class);
    private static final String PAGE_TYPE_PROPERTY_PATH = "jcr:content@pageType";
    private static final String IMAGE_PROPERTY_PATH = "cq:featuredimage@fileReference";
    private static final String PRODUCT_LINE_PAGE_TYPE ="productLineLandingPage";
	
	/*Manufacturer Page*/
    @Self
    Page page;

    /*Manufacturer Title*/
    @NotNull
    @Getter
    private String title;
    
    /*Manufacturer Image*/
    @NotNull
    @Getter
    private String imageSrc;
    
    /*Manufacturer Details Page Path*/
    @Nullable
    @Getter
    private String redirectLink;

    /*Product Line Page Cards*/
    @Nullable
    @Getter
    private List<ProductLineCard> productLines;
    
    @PostConstruct
    public void init() {
    	//To-Do: Replace this logic to inject the necessary attributes from page props
    	LOGGER.trace("Initialising Manufacturer Card Model: {}", page);
    	this.title = page.getTitle();
    	this.redirectLink = page.getPath();
    	//To-Do: Change logic to return featured image
    	this.imageSrc = MacnicaComponentUtils.getPagePropertyValue(page, IMAGE_PROPERTY_PATH);
        this.productLines = getProductLinePages(page).stream()
        .map(productLinePage -> productLinePage.adaptTo(ProductLineCard.class))
        .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Method to return List of Product Line Pages under Manufacturer Pages
     *
     * @param page Manufacturer Page
     * @return productLinePages List<Page>
     */
    private List<Page> getProductLinePages(Page page) {
        List<Page> productLinePages = new ArrayList<>();
        Iterator<Page> childPages = page.listChildren();
        
        while(childPages.hasNext()) {
            Page childPage = childPages.next();
            String pageType = MacnicaComponentUtils.getPagePropertyValue(childPage, PAGE_TYPE_PROPERTY_PATH);
            if (pageType.equalsIgnoreCase(PRODUCT_LINE_PAGE_TYPE)) {
                productLinePages.add(childPage);
            }
        }
        
        Collections.sort(productLinePages, Comparator.comparing(Page::getPath));
        return productLinePages;
    }
}