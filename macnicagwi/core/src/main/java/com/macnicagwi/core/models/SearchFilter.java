package com.macnicagwi.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import com.day.cq.tagging.Tag;
import com.drew.lang.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * {@code SearchFilter} Sling Model 
 * Adaptable to Tag.
 * @author Sumit
 */

@Model(adaptables = { SlingHttpServletRequest.class, Tag.class })
@JsonInclude(Include.NON_NULL)
public class SearchFilter {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchFilter.class);
	
	/*Tag*/
    @Self
    Tag tag;

    /*Tag ID*/
    @NotNull
    @Getter
    private String id;
    
    /*Tag Title*/
    @NotNull
    @Getter
    private String title;

    @Getter
    List<SearchFilter> childSearchFilters;

    SearchFilter() {
        // Empty Default Constructor
    }

    // TO-DO: Replace this constructor with dependency injection
    SearchFilter(@NotNull String id, @NotNull String title) {
        this.id = id;
        this.title = title;
    }
    
    @PostConstruct
    public void init() {
    	//To-Do: Replace this logic to inject the necessary attributes from tag props
    	LOGGER.trace("Initialising Search Facet Model: {}", tag);
        this.id = tag.getTagID();
    	this.title = tag.getTitle();
        this.childSearchFilters = getChildSearchFilters(tag);	
    }

    private List<SearchFilter> getChildSearchFilters(Tag tag) {
        Iterator<Tag> childTags = tag.listChildren();
        List<SearchFilter> childFilters = new ArrayList<>();
                
        while(childTags.hasNext()) {
            Tag childTag = childTags.next();
            childFilters.add(new SearchFilter(childTag.getTagID(), childTag.getTitle()));
        }
        return childFilters;
    }
}
