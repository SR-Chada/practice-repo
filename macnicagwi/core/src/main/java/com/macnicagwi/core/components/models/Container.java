package com.macnicagwi.core.components.models;

import org.apache.commons.lang3.StringUtils;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * {@code Container} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/form/container} component.
 * @author Sumit
 */
@ConsumerType
public interface Container extends com.adobe.cq.wcm.core.components.models.form.Container {
	default String getSuccessFragmentPath() {
        return StringUtils.EMPTY;
    }

    default String getErrorFragmentPath() {
        return StringUtils.EMPTY;
    }

    default String getClientKey(){
        return StringUtils.EMPTY;
    }

    default String getEventId(){
        return StringUtils.EMPTY;
    }

    default String getChildEventId(){
        return StringUtils.EMPTY;
    }
}
