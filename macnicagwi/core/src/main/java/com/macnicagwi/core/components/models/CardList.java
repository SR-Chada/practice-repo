package com.macnicagwi.core.components.models;

import java.util.Collection;

import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.sling.api.resource.Resource;

/**
 * {@code CardList} Sling Model. 
 * Used for the {@code /apps/macnicagwi/components/content/cardlist} component.
 * @author Sai
 */

public interface CardList extends Component {

    /**
     * @return a collection of objects representing the items that compose the the list.
     */
	@JsonIgnore
    Collection<MacnicaPageListItem> getListItems();

    /**
     * @return true if this component has no list items to display.
     */
    boolean isEmpty();

    /**
     * 
     * @return JSON data to populate the data layer
     */
    @Override
    @JsonProperty("dataLayer")
    default ComponentData getData() {
        return null;
    }

    /**
     * 
     * @return String representing the unique identifier of the ImageList component on a page
     */
    @Override
    @JsonProperty("id")
    String getId();

    /**
     * @return Path to the current card list component resource
     */
    @JsonProperty("path")
	String getResourcePath();

    /**
     * Provides the total number of relevant pages obtained for requested search. 
     * This value should be used to paginate card list results. 
     * @return Total number of cards fetched.
     */
	Integer getTotalResultsSize();
	
	public Boolean showDescription();
	public Boolean showTitle();
	public Boolean showFeaturedImage();
	public Boolean showArticleDate();
	public Boolean linkItems();
	public boolean isfreeTextSearchEnabled();
	

	public String getEventLocationLabel();
	public String getEventStartDateLabel();
	public String getEventEndDateLabel();
	public String getEventStatusLabel();
    
    public Boolean showManufacturerLogo();

    

}