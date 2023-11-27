package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;

/**
 * {@code Heading} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/heading} component.
 * @author Sai
 */
@ConsumerType
public interface Heading extends Component {
	
	/**
     * Returns the Heading text.
     * @return the Heading text
     */
    default String getText() {
        return null;
    }

    /**
     * Returns the Heading icon identifier.
     * @return the Heading icon identifier
     */
    default String getIcon() {
        return null;
    }
    
    /**
     * Returns the Heading type.
     * @return the Heading type
     */
    default String getType() {
        return null;
    }
    
    /**
     * Returns the Heading type.
     * @return the Heading type
     */
    default String getCssClassName() {
        return null;
    }
    
    



}
