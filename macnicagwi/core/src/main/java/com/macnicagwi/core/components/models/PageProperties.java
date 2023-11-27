package com.macnicagwi.core.components.models;

import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;

/**
 * {@code PageProperties} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/pageproperties} component.
 * @author Sumit
 */
@ConsumerType
public interface PageProperties extends Component {
	
	/**
     * Returns the News Date.
     * @return the News Date
     */
    default String getNewsDate() {
        return null;
    }

    /**
     * Returns the News Date Label.
     * @return the News Date Label
     */
    default String getNewsDateLabel() {
        return null;
    }

	/**
     * Returns the Event Start Date.
     * @return the Event Start Date
     */
    default String getEventStartDate() {
	    return null;
	}
	
	/**
     * Returns the Event Start Date Label.
     * @return the Event Start Date Label
     */
	default String getEventStartDateLabel() {
	    return null;
	}

	/**
     * Returns the Event End Date.
     * @return the Event End Date
     */
	default String getEventEndDate() {
	    return null;
	}
	
	/**
     * Returns the Event End Date Label.
     * @return the Event End Date Label
     */
	default String getEventEndDateLabel() {
	    return null;
	}

	/**
     * Returns the Event Location.
     * @return the Event Location
     */
	default String getEventLocation() {
	    return null;
	}
	
	/**
     * Returns the Event Location Label.
     * @return the Event Location Label
     */
	default String getEventLocationLabel() {
	    return null;
	}

    /**
     * Returns the Technical Article Date.
     * @return the Technical Article Date
     */
	default String getTechnicalArticleDate() {
	    return null;
    }

    default String getAuthorName() {
	    return null;
    }

    public Resource getManufacturerLogo();

    default String getAuthorNameLabel() {
        return null;
    }
    //articlesource
    default String getArticleSource() {
	    return null;
    }

    default String getArticleSourceLabel() {
        return null;
    }

    /**
     * Returns the Technical Article Date Label.
     * @return the Technical Article Date Label
     */
    default String getTechnicalArticleDateLabel() {
        return null;
    }

	public Resource getAuthorImage();
}
