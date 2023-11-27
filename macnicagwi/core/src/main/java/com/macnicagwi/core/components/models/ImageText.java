package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;

/**
 * {@code ImageText} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/imagetext} component.
 * @author Sai
 */
@ConsumerType
public interface ImageText extends Component {
	
	/**
     * Returns the ImageText text.
     * @return the ImageText text
     */
    default boolean isText() {
        return false;
    }

    /**
     * Returns the ImageText Button.
     * @return the ImageText Button
     */
    default boolean isButton() {
        return false;
    }
    
    /**
     * Returns the ImageText Heading.
     * @return the ImageText Heading
     */
    default boolean isHeading() {
        return false;
    }

    /**
     * Returns the ImageText AssetType.
     * @return the ImageText AssetType
     */
    String getAssetType();
    
    /**
     * Returns the ImageText AssetPositionLargeScreen.
     * @return the ImageText AssetPositionLargeScreen.
    */
    String getAssetPositionLargeScreen();

    /**
     * Returns the ImageText AssetPositionMediumScreen.
     * @return the ImageText AssetPositionMediumScreen.
    */
    String getAssetPositionMediumScreen();

    /**
     * Returns the ImageText AssetPositionSmallScreen.
     * @return the ImageText AssetPositionSmallScreen.
    */
    String getAssetPositionSmallScreen();
}
