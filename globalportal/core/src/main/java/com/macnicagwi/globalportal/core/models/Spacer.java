package com.macnicagwi.globalportal.core.models;

import org.osgi.annotation.versioning.ProviderType;

import com.adobe.cq.wcm.core.components.models.Component;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Sling model for {@link globalportal/components/spacer} component
 * @author Sai
 *
 */
@ProviderType
public interface Spacer extends Component{
	
    /**
     * Returns the style system information associated with the component
     *
     * @return CSS classes selected by the content author delimited using a SPACE character
     *
     * @since com.adobe.cq.wcm.core.components.models 12.20.0
     */
    @Nullable
    @JsonProperty("appliedCssClassNames")
    default String getAppliedCssClasses() {
        return null;
    }

}
