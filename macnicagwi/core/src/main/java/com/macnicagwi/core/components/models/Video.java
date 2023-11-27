package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Embed;

/**
 * {@code Video} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/video} component.
 * @author Sumit
 */
@ConsumerType
public interface Video extends Embed {

    /**
     * Returns Is Video Restricted.
     * @return Is Video Restricted
     */
    boolean isRestrictedVideo();

    /**
     * Returns Is Valid Request.
     * @return Is Valid Request
     */
    boolean isValidRequest();

    default String getVideoUrl() {
        return null;
    }

    int getWidth();
    int getHeight();
	
    default String getEmbedCode() {
        return null;
    }

    int getEmbedWidth();
    int getEmbedHeight();
}
