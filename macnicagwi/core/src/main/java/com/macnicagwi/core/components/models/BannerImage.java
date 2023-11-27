package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.models.Image;
import com.drew.lang.annotations.Nullable;

/**
 * {@code BannerImage} Sling Model. 
 * used for the {@code /apps/macnicagwi/components/content/bannerimage} component.
 * @author Sai
 */

@ConsumerType
public interface BannerImage extends Component {
	

	
    /**
     * Returns the subtext of banner image component.
     * @return the subtext authored in the banner image component
     */
	default String getSubText() {
        return null;
    }

    
    /**
     * Returns a boolean flag if the image should be displayed as a popup or not
     * @return 
     * 	@true if the popup option is checked in component dialogue; 
     * 	@False otherwise.
     */
    default boolean isPopup() {
        return false;
    }
    
    /**
     * @return the Image Sling Model of this resource, 
     * or null if the resource cannot create a valid Image Sling Model.
     */
     public Image getImage();
    
    /***
    * @return a boolean if the component has enough content to display.
    */
    public boolean isEmpty();




	
	/**
	 * Returns the Link if configured in the image dialogue 
	 * or redirect page resource (used in case of listing component).
	 * @return Link object
	 */
	 @SuppressWarnings("rawtypes")
	@Nullable
		public default Link getImageLink(){
	    	return null;
	    }
	
	
	

}
